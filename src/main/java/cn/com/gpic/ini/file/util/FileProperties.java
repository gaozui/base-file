package cn.com.gpic.ini.file.util;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import java.io.File;

/**
 * @author lzk&yjj
 */
@ConfigurationProperties(prefix = "file")
public class FileProperties {

    public static final String PREFIX = "file";
    public static final String ENABLE_LOCAL_STORAGE = "file.storage.local-file-storage.enable";
    public static final String ENABLE_MINIO_STORAGE = "file.storage.minio-file-storage.enable";
    private File tempDirectory = SystemUtils.getJavaIoTmpDir();
    private boolean sha256 = true;
    private boolean compress = true;
    private FileEncryptProperties encrypt = new FileEncryptProperties();
    private FileThumbnailsProperties thumbnails = new FileThumbnailsProperties(true, true, 200, 200);
    private FileStorageProperties storage = new FileStorageProperties();

    public FileProperties() {
    }

    public File getTempDirectory() {
        return this.tempDirectory;
    }

    public void setTempDirectory(File tempDirectory) {
        this.tempDirectory = tempDirectory;
    }

    public boolean isSha256() {
        return this.sha256;
    }

    public void setSha256(boolean sha256) {
        this.sha256 = sha256;
    }

    public boolean isCompress() {
        return this.compress;
    }

    public void setCompress(boolean compress) {
        this.compress = compress;
    }

    public FileEncryptProperties getEncrypt() {
        return this.encrypt;
    }

    public void setEncrypt(FileEncryptProperties encrypt) {
        this.encrypt = encrypt;
    }

    public FileThumbnailsProperties getThumbnails() {
        return this.thumbnails;
    }

    public void setThumbnails(FileThumbnailsProperties thumbnails) {
        this.thumbnails = thumbnails;
    }

    public FileStorageProperties getStorage() {
        return this.storage;
    }

    public void setStorage(FileStorageProperties storage) {
        this.storage = storage;
    }

    public static class FileStorageProperties {
        private LocalFileStorageProperties localFileStorage = new LocalFileStorageProperties();
        private MinioFileStorageProperties minioFileStorage;

        public FileStorageProperties() {
        }

        public LocalFileStorageProperties getLocalFileStorage() {
            return this.localFileStorage;
        }

        public void setLocalFileStorage(LocalFileStorageProperties localFileStorage) {
            this.localFileStorage = localFileStorage;
        }

        public MinioFileStorageProperties getMinioFileStorage() {
            return this.minioFileStorage;
        }

        public void setMinioFileStorage(MinioFileStorageProperties minioFileStorage) {
            this.minioFileStorage = minioFileStorage;
        }

        public static class MinioFileStorageProperties {
            private boolean enable = true;
            @NotBlank(
                    message = "minio endpoint {javax.validation.constraints.NotBlank.message}"
            )
            private String endpoint;
            @NotBlank(
                    message = "minio accessKey {javax.validation.constraints.NotBlank.message}"
            )
            private String accessKey;
            @NotBlank(
                    message = "minio secretKey {javax.validation.constraints.NotBlank.message}"
            )
            private String secretKey;
            @Nullable
            private String bucket;

            public MinioFileStorageProperties() {
            }

            public boolean isEnable() {
                return this.enable;
            }

            public void setEnable(boolean enable) {
                this.enable = enable;
            }

            public String getEndpoint() {
                return this.endpoint;
            }

            public void setEndpoint(String endpoint) {
                this.endpoint = endpoint;
            }

            public String getAccessKey() {
                return this.accessKey;
            }

            public void setAccessKey(String accessKey) {
                this.accessKey = accessKey;
            }

            public String getSecretKey() {
                return this.secretKey;
            }

            public void setSecretKey(String secretKey) {
                this.secretKey = secretKey;
            }

            @Nullable
            public String getBucket() {
                return this.bucket;
            }

            public void setBucket(@Nullable String bucket) {
                this.bucket = bucket;
            }
        }

        public static class LocalFileStorageProperties {
            private boolean enable = true;
            @Nullable
            private String directory;

            public LocalFileStorageProperties() {
            }

            public boolean isEnable() {
                return this.enable;
            }

            public void setEnable(boolean enable) {
                this.enable = enable;
            }

            @Nullable
            public String getDirectory() {
                return this.directory;
            }

            public void setDirectory(@Nullable String directory) {
                this.directory = directory;
            }
        }
    }

    public static class FileThumbnailsProperties {
        private boolean enable = true;
        private Boolean keepAspectRatio;
        private Integer width;
        private Integer height;

        public FileThumbnailsProperties() {
        }

        public FileThumbnailsProperties(boolean enable, Boolean keepAspectRatio, Integer width, Integer height) {
            this.enable = enable;
            this.keepAspectRatio = keepAspectRatio;
            this.width = width;
            this.height = height;
        }

        public boolean isEnable() {
            return this.enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public Boolean getKeepAspectRatio() {
            return this.keepAspectRatio;
        }

        public void setKeepAspectRatio(Boolean keepAspectRatio) {
            this.keepAspectRatio = keepAspectRatio;
        }

        public Integer getWidth() {
            return this.width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return this.height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }
    }

    public static class FileEncryptProperties {
        private boolean enable = true;
        private SymmetricAlgorithm algorithm;
        @Nullable
        private String key;

        public FileEncryptProperties() {
            this.algorithm = SymmetricAlgorithm.AES;
        }

        public boolean isEnable() {
            return this.enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public SymmetricAlgorithm getAlgorithm() {
            return this.algorithm;
        }

        public void setAlgorithm(SymmetricAlgorithm algorithm) {
            this.algorithm = algorithm;
        }

        @Nullable
        public String getKey() {
            return this.key;
        }

        public void setKey(@Nullable String key) {
            this.key = key;
        }
    }
}
