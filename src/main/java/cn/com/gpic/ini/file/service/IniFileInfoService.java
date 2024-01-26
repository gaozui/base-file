package cn.com.gpic.ini.file.service;

import cn.com.gpic.ini.file.entity.IniFileInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 文件 服务类
 * </p>
 *
 * @author lzk&yjj
 * @since 2022-10-20 12:11:24
 */
public interface IniFileInfoService extends IService<IniFileInfo> {

    /**
     * 保存文件
     *
     * @param file 文件
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    IniFileInfo saveFile(@Nonnull File file);

    /**
     * 保存文件
     *
     * @param file               文件
     * @param generateThumbnails 文件为图片时, 是否生成缩略图
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    IniFileInfo saveFile(@Nonnull File file, boolean generateThumbnails);

    /**
     * 保存文件
     *
     * @param fileInfo 文件信息
     * @param file     文件
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    IniFileInfo saveFile(@Nonnull IniFileInfo fileInfo, @Nonnull File file);

    /**
     * 保存文件
     *
     * @param fileInfo           文件信息
     * @param file               文件
     * @param generateThumbnails 文件为图片时, 是否生成缩略图
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    IniFileInfo saveFile(@Nonnull IniFileInfo fileInfo, @Nonnull File file, boolean generateThumbnails);

    /**
     * 保存文件
     *
     * @param files 文件
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    List<IniFileInfo> saveFile(@Nonnull File... files);

    /**
     * 保存文件
     *
     * @param generateThumbnails 文件为图片时, 是否生成缩略图
     * @param files              文件
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    List<IniFileInfo> saveFile(boolean generateThumbnails, @Nonnull File... files);

    /**
     * 保存文件
     *
     * @param path 文件路径
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    IniFileInfo saveFile(@Nonnull Path path);

    /**
     * 保存文件
     *
     * @param path               文件路径
     * @param generateThumbnails 文件为图片时, 是否生成缩略图
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    IniFileInfo saveFile(@Nonnull Path path, boolean generateThumbnails);

    /**
     * 保存文件
     *
     * @param fileInfo 文件信息
     * @param path     文件路径
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    IniFileInfo saveFile(@Nonnull IniFileInfo fileInfo, @Nonnull Path path);

    /**
     * 保存文件
     *
     * @param fileInfo           文件信息
     * @param path               文件路径
     * @param generateThumbnails 文件为图片时, 是否生成缩略图
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    IniFileInfo saveFile(@Nonnull IniFileInfo fileInfo, @Nonnull Path path, boolean generateThumbnails);

    /**
     * 保存文件
     *
     * @param paths 文件路径
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    List<IniFileInfo> saveFile(@Nonnull Path... paths);

    /**
     * 保存文件
     *
     * @param generateThumbnails 文件为图片时, 是否生成缩略图
     * @param paths              文件路径
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    List<IniFileInfo> saveFile(boolean generateThumbnails, @Nonnull Path... paths);

    /**
     * 保存文件
     *
     * @param file 文件
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    IniFileInfo saveFile(@Nonnull MultipartFile file);

    /**
     * 保存文件
     *
     * @param file               文件
     * @param generateThumbnails 文件为图片时, 是否生成缩略图
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    IniFileInfo saveFile(@Nonnull MultipartFile file, boolean generateThumbnails);

    /**
     * 保存文件
     *
     * @param fileInfo 文件信息
     * @param file     文件
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    IniFileInfo saveFile(@Nonnull IniFileInfo fileInfo, @Nonnull MultipartFile file);

    /**
     * 保存文件
     *
     * @param fileInfo           文件信息
     * @param file               文件
     * @param generateThumbnails 文件为图片时, 是否生成缩略图
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    IniFileInfo saveFile(@Nonnull IniFileInfo fileInfo, @Nonnull MultipartFile file, boolean generateThumbnails);

    /**
     * 保存文件
     *
     * @param files 文件
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    List<IniFileInfo> saveFile(@Nonnull MultipartFile... files);

    /**
     * 保存文件
     *
     * @param generateThumbnails 文件为图片时, 是否生成缩略图
     * @param files              文件
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    List<IniFileInfo> saveFile(boolean generateThumbnails, @Nonnull MultipartFile... files);

    /**
     * 保存文件
     *
     * @param stream 文件流
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    IniFileInfo saveFile(@Nonnull InputStream stream);

    /**
     * 保存文件
     *
     * @param stream             文件流
     * @param generateThumbnails 文件为图片时, 是否生成缩略图
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    IniFileInfo saveFile(@Nonnull InputStream stream, boolean generateThumbnails);

    /**
     * 保存文件
     *
     * @param fileInfo 文件信息
     * @param stream   文件流
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    IniFileInfo saveFile(@Nonnull IniFileInfo fileInfo, @Nonnull InputStream stream);

    /**
     * 保存文件
     *
     * @param fileInfo           文件信息
     * @param stream             文件流
     * @param generateThumbnails 文件为图片时, 是否生成缩略图
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    IniFileInfo saveFile(@Nonnull IniFileInfo fileInfo, @Nonnull InputStream stream, boolean generateThumbnails);

    /**
     * 保存文件
     *
     * @param streams 文件流
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    List<IniFileInfo> saveFile(@Nonnull InputStream... streams);

    /**
     * 保存文件
     *
     * @param generateThumbnails 文件为图片时, 是否生成缩略图
     * @param streams            文件流
     * @return 文件信息
     */
    @CanIgnoreReturnValue
    List<IniFileInfo> saveFile(boolean generateThumbnails, @Nonnull InputStream... streams);

    /**
     * 删除文件
     *
     * @param fileId 文件ID
     */
    void deleteFile(@Nonnull String fileId);

    /**
     * 删除文件
     *
     * @param fileId     文件ID
     * @param thumbnails 是否仅查询缩略图
     */
    void deleteFile(@Nonnull String fileId, boolean thumbnails);

    /**
     * 删除文件
     *
     * @param fileInfo 文件信息
     */
    void deleteFile(@Nonnull IniFileInfo fileInfo);

    /**
     * 删除文件
     *
     * @param fileIds 文件ID
     */
    void deleteFile(@Nonnull String... fileIds);

    /**
     * 删除文件
     *
     * @param thumbnails 是否仅查询缩略图
     * @param fileIds    文件ID
     */
    void deleteFile(boolean thumbnails, @Nonnull String... fileIds);

    /**
     * 获取文件
     *
     * @param fileId 文件ID
     * @return 文件
     */
    Optional<File> getFile(@Nonnull String fileId);

    /**
     * 获取文件
     *
     * @param fileId     文件ID
     * @param thumbnails 是否仅查询缩略图
     * @return 文件
     */
    Optional<File> getFile(@Nonnull String fileId, boolean thumbnails);

    /**
     * 获取文件
     *
     * @param fileInfo 文件信息
     * @return 文件
     */
    Optional<File> getFile(@Nonnull IniFileInfo fileInfo);

    /**
     * 获取文件流
     *
     * @param fileId 文件ID
     * @return 文件流
     */
    Optional<InputStream> getFileStream(@Nonnull String fileId);

    /**
     * 获取文件流
     *
     * @param fileId     文件ID
     * @param thumbnails 是否仅查询缩略图
     * @return 文件流
     */
    Optional<InputStream> getFileStream(@Nonnull String fileId, boolean thumbnails);

    /**
     * 获取文件流
     *
     * @param fileInfo 文件信息
     * @return 文件流
     */
    Optional<InputStream> getFileStream(@Nonnull IniFileInfo fileInfo);

    /**
     * 打包文件
     *
     * @param fileIds 文件ID
     * @return 压缩文件
     */
    File zip(@Nonnull String... fileIds);

    /**
     * 打包文件
     *
     * @param thumbnails 是否仅查询缩略图
     * @param fileIds    文件ID
     * @return 压缩文件
     */
    File zip(boolean thumbnails, @Nonnull String... fileIds);

    /**
     * 打包文件
     *
     * @param fileName 压缩文件名
     * @param fileIds  文件ID
     * @return 压缩文件
     */
    File zip(@Nullable String fileName, @Nonnull String... fileIds);

    /**
     * 打包文件
     *
     * @param fileName   压缩文件名
     * @param thumbnails 是否仅查询缩略图
     * @param fileIds    文件ID
     * @return 压缩文件
     */
    File zip(@Nullable String fileName, boolean thumbnails, @Nonnull String... fileIds);

    /**
     * 下载文件
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param fileId   文件ID
     */
    void download(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull String fileId);

    /**
     * 下载文件
     *
     * @param request    请求对象
     * @param response   响应对象
     * @param fileId     文件ID
     * @param thumbnails 是否仅查询缩略图
     */
    void download(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull String fileId, boolean thumbnails);

    /**
     * 下载文件
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param fileName 下载文件名
     * @param fileId   文件ID
     */
    void download(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nullable String fileName, @Nonnull String fileId);

    /**
     * 下载文件
     *
     * @param request    请求对象
     * @param response   响应对象
     * @param fileName   下载文件名
     * @param thumbnails 是否仅查询缩略图
     * @param fileId     文件ID
     */
    void download(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nullable String fileName, boolean thumbnails, @Nonnull String fileId);

    /**
     * 下载文件
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param fileIds  文件ID
     */
    void download(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull String... fileIds);

    /**
     * 下载文件
     *
     * @param request    请求对象
     * @param response   响应对象
     * @param thumbnails 是否仅查询缩略图
     * @param fileIds    文件ID
     */
    void download(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, boolean thumbnails, @Nonnull String... fileIds);

    /**
     * 文件ID
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param fileName 下载文件名
     * @param fileIds  文件ID
     */
    void download(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nullable String fileName, @Nonnull String... fileIds);

    /**
     * 文件ID
     *
     * @param request    请求对象
     * @param response   响应对象
     * @param fileName   下载文件名
     * @param thumbnails 是否仅查询缩略图
     * @param fileIds    文件ID
     */
    void download(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nullable String fileName, boolean thumbnails, @Nonnull String... fileIds);

    /**
     * 预览文件
     *
     * @param response 响应对象
     * @param fileId   文件ID
     */
    void preview(@Nonnull HttpServletResponse response, @Nonnull String fileId);

    /**
     * 预览文件
     *
     * @param response   响应对象
     * @param thumbnails 是否仅查询缩略图
     * @param fileId     文件ID
     */
    void preview(@Nonnull HttpServletResponse response, boolean thumbnails, @Nonnull String fileId);

    /**
     * 获取文件信息
     *
     * @param id         文件ID
     * @param thumbnails 是否仅查询缩略图
     * @return 文件信息
     */
    @Nullable
    IniFileInfo getById(@Nonnull Serializable id, boolean thumbnails);

    /**
     * 获取文件信息
     *
     * @param idList     文件ID
     * @param thumbnails 是否仅查询缩略图
     * @return 文件信息
     */
    List<IniFileInfo> listByIds(@Nonnull Iterable<? extends Serializable> idList, boolean thumbnails);

    /**
     * 获取缓存文件目录
     *
     * @return 缓存文件目录
     */
    File getTempDirectory();

    /**
     * 创建缓存文件
     *
     * @param prefix 文件前缀
     * @param suffix 文件后缀
     * @param attrs  文件属性
     * @return 缓存文件
     */
    File createTempFile(@Nullable String prefix, @Nullable String suffix, FileAttribute<?>... attrs);

    /**
     * 创建缓存文件夹
     *
     * @param prefix 文件夹前缀
     * @param attrs  文件夹属性
     * @return 缓存文件夹
     */
    File createTempDirectory(@Nullable String prefix, FileAttribute<?>... attrs);

    /**
     * 尝试为文件生成缩略图
     *
     * @param fileInfo    文件信息
     * @param inputStream 文件流
     * @return 缩略图文件信息
     * @throws IOException 创建缩略图可能抛出的异常
     */
    @Nullable
    @CanIgnoreReturnValue
    IniFileInfo generateThumbnails(@Nonnull IniFileInfo fileInfo, InputStream inputStream) throws IOException;

    /**
     * 创建缩略图
     *
     * @param image      图片
     * @param thumbnails 缩略图文件
     * @throws IOException 创建缩略图可能抛出的异常
     */
    void createThumbnails(BufferedImage image, File thumbnails) throws IOException;

    /**
     * 压缩文件
     *
     * @param inputStream 文件流
     * @return 压缩文件流
     * @throws IOException 压缩文件可能抛出的异常
     */
    InputStream compress(InputStream inputStream) throws IOException;

    /**
     * 加密文件
     *
     * @param inputStream 文件流
     * @return 加密文件流
     * @throws InvalidKeyException 校验密钥可能抛出的异常
     */
    InputStream encrypt(InputStream inputStream) throws InvalidKeyException;

    /**
     * 解压文件
     *
     * @param inputStream 文件流
     * @return 解压文件流
     * @throws IOException 解压文件可能抛出的异常
     */
    InputStream decompress(InputStream inputStream) throws IOException;

    /**
     * 解密文件
     *
     * @param inputStream 文件流
     * @return 解密文件流
     * @throws InvalidKeyException 校验密钥可能抛出的异常
     * @throws IOException         输出文件可能抛出的异常
     */
    InputStream decrypt(InputStream inputStream) throws InvalidKeyException, IOException;
}
