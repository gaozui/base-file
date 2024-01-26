package cn.com.gpic.ini.file.service.impl;

import cn.com.gpic.ini.file.entity.IniFileInfo;
import cn.com.gpic.ini.file.service.IniFileStorageService;
import cn.com.gpic.ini.file.util.FileProperties;
import cn.com.gpic.ini.file.util.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

/**
 * @author lzk&yjj
 */
@Service
@ConditionalOnProperty(value = {"file.storage.local-file-storage.enable"}, matchIfMissing = true)
public class IniLocalFileStorageServiceImpl implements IniFileStorageService {

    private final FileProperties.FileStorageProperties.LocalFileStorageProperties localFileStorageProperties;

    @Autowired
    public IniLocalFileStorageServiceImpl(FileProperties fileProperties) {
        this.localFileStorageProperties = fileProperties.getStorage().getLocalFileStorage();
    }

    @Override
    public void saveFileStream(@NotNull IniFileInfo fileInfo, @NotNull InputStream inputStream) throws Exception {
        File file = this.getFile(fileInfo);
        FileUtils.forceMkdirParent(file);
        OutputStream outputStream = IOUtils.buffer(FileUtils.openOutputStream(file));
        Throwable var5 = null;
        try {
            IOUtils.copy(inputStream, outputStream);
        } catch (Throwable var14) {
            var5 = var14;
            throw var14;
        } finally {
            if (var5 != null) {
                try {
                    outputStream.close();
                } catch (Throwable var13) {
                    var5.addSuppressed(var13);
                }
            } else {
                outputStream.close();
            }
        }
    }

    @Nullable
    @Override
    public InputStream getFileStream(@NotNull IniFileInfo fileInfo) throws Exception {
        File file = this.getFile(fileInfo);
        return !file.exists() ? null : IOUtils.buffer(FileUtils.openInputStream(file));
    }

    @Override
    public void deleteFile(@Nonnull IniFileInfo fileInfo) {
        File file = this.getFile(fileInfo);
        FileUtils.deleteQuietly(file);
    }

    @Nonnull
    private File getDirectory(@Nonnull IniFileInfo fileInfo) {
        StringBuilder sb = new StringBuilder();
        Optional.ofNullable(this.localFileStorageProperties).map(FileProperties.FileStorageProperties.LocalFileStorageProperties::getDirectory).ifPresent((file) -> sb.append(file).append(File.separatorChar));
        sb.append(fileInfo.getPath());
        return new File(sb.toString());
    }

    @Nonnull
    private File getFile(@Nonnull IniFileInfo fileInfo) {
        return new File(this.getDirectory(fileInfo), String.format("%d.%s", Long.parseLong(fileInfo.getId()), "1".equals(fileInfo.getIsCompress()) ? "gz" : FilenameUtils.getExtension(fileInfo.getName())));
    }

}
