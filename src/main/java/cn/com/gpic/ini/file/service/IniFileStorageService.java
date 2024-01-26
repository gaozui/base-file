package cn.com.gpic.ini.file.service;

import cn.com.gpic.ini.file.entity.IniFileInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.InputStream;

/**
 * @author lzk&yjj
 */
public interface IniFileStorageService {

    void saveFileStream(@Nonnull IniFileInfo fileInfo, @Nonnull InputStream inputStream) throws Exception;

    @Nullable
    InputStream getFileStream(@Nonnull IniFileInfo fileInfo) throws Exception;

    void deleteFile(@Nonnull IniFileInfo fileInfo) throws Exception;

}
