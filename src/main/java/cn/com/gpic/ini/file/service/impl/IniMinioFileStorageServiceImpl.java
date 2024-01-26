package cn.com.gpic.ini.file.service.impl;

import cn.com.gpic.ini.file.entity.IniFileInfo;
import cn.com.gpic.ini.file.service.IniFileStorageService;
import cn.com.gpic.ini.file.util.FileProperties;
import io.minio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.Optional;

@Service
@ConditionalOnBean({MinioClient.class})
@ConditionalOnClass({MinioClient.class})
@ConditionalOnProperty(value = {"file.storage.minio-file-storage.enable"}, matchIfMissing = true)
public class IniMinioFileStorageServiceImpl implements IniFileStorageService {
    private final MinioClient minioClient;
    private final FileProperties.FileStorageProperties.MinioFileStorageProperties minioFileStorage;

    @Autowired
    public IniMinioFileStorageServiceImpl(MinioClient minioClient, FileProperties fileProperties) {
        this.minioClient = minioClient;
        this.minioFileStorage = fileProperties.getStorage().getMinioFileStorage();
    }

    @Override
    public void saveFileStream(@Nonnull IniFileInfo fileInfo, @Nonnull InputStream inputStream) throws Exception {
        String bucket = this.getBucket();
        if (!this.minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
            this.minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }

        this.minioClient.putObject(PutObjectArgs.builder().bucket(bucket).object(fileInfo.getPath()).contentType(fileInfo.getMimeType()).stream(inputStream, Long.parseLong(fileInfo.getFileSize()), -1L).build());
    }

    @Override
    @Nullable
    public InputStream getFileStream(@Nonnull IniFileInfo fileInfo) throws Exception {
        String bucket = this.getBucket();
        return this.minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build()) ? this.minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(fileInfo.getPath()).build()) : null;
    }

    @Override
    public void deleteFile(@Nonnull IniFileInfo fileInfo) throws Exception {
        String bucket = this.getBucket();
        if (this.minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
            this.minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(fileInfo.getPath()).build());
        }

    }

    @Nonnull
    private String getBucket() {
        return Optional.ofNullable(this.minioFileStorage).map(FileProperties.FileStorageProperties.MinioFileStorageProperties::getBucket).orElse("base-system");
    }

}
