package cn.com.gpic.ini.file.service.impl;

import cn.com.gpic.ini.common.util.date.DateUtils;
import cn.com.gpic.ini.file.entity.IniFileInfo;
import cn.com.gpic.ini.file.mapper.IniFileInfoMapper;
import cn.com.gpic.ini.file.service.IniFileInfoService;
import cn.com.gpic.ini.file.service.IniFileStorageService;
import cn.com.gpic.ini.file.util.FileProperties;
import cn.com.gpic.ini.file.util.FileUtils;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import io.github.vampireachao.stream.plugin.mybatisplus.Database;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.davidmoten.io.extras.IOUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.ContentDisposition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.security.DigestInputStream;
import java.security.InvalidKeyException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * <p>
 * 文件 服务实现类
 * </p>
 *
 * @author Gpic
 * @since 2022-10-20 12:11:24
 */
@Service
public class IniFileInfoServiceImpl extends ServiceImpl<IniFileInfoMapper, IniFileInfo> implements IniFileInfoService {

    @Resource
    private FileProperties fileProperties;

    @Resource
    private ObjectProvider<IniFileStorageService> fileStorageServices;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public IniFileInfo saveFile(@Nonnull File file) {
        return this.saveFile(new IniFileInfo(), file, false);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public IniFileInfo saveFile(@Nonnull File file, boolean generateThumbnails) {
        return this.saveFile(new IniFileInfo(), file, generateThumbnails);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public IniFileInfo saveFile(@Nonnull IniFileInfo fileInfo, @Nonnull File file) {
        return this.saveFile(fileInfo, file, false);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public IniFileInfo saveFile(@Nonnull IniFileInfo fileInfo, @Nonnull File file, boolean generateThumbnails) {
        this.getDefaultIfNull(fileInfo, fileInfo.getName(), fileInfo::setName, (entity) -> file.getName());
        this.getDefaultIfNull(fileInfo, fileInfo.getMimeType(), fileInfo::setMimeType, (entity) -> StringUtils.defaultString(FileUtils.getContentType(file), "application/octet-stream"));
        this.getDefaultIfNull(fileInfo, fileInfo.getFileSize(), fileInfo::setFileSize, (entity) -> Long.toString(file.length()));
        try {
            InputStream inputStream = FileUtils.openInputStream(file);
            IniFileInfo var6;
            try {
                var6 = this.saveFile(fileInfo, inputStream, generateThumbnails);
            } finally {
                inputStream.close();
            }
            return var6;
        } catch (IOException var18) {
            throw new RuntimeException(var18.getMessage(), var18);
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public List<IniFileInfo> saveFile(@Nonnull File... files) {
        return this.saveFile(false, files);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public List<IniFileInfo> saveFile(boolean generateThumbnails, @Nonnull File... files) {
        List<IniFileInfo> list = Lists.newArrayListWithCapacity(files.length);
        for (File file : files) {
            list.add(this.saveFile(file, generateThumbnails));
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public IniFileInfo saveFile(@Nonnull Path path) {
        return this.saveFile(new IniFileInfo(), path.toFile(), false);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public IniFileInfo saveFile(@Nonnull Path path, boolean generateThumbnails) {
        return this.saveFile(new IniFileInfo(), path.toFile(), generateThumbnails);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public IniFileInfo saveFile(@Nonnull IniFileInfo fileInfo, @Nonnull Path path) {
        return this.saveFile(fileInfo, path.toFile(), false);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public IniFileInfo saveFile(@Nonnull IniFileInfo fileInfo, @Nonnull Path path, boolean generateThumbnails) {
        return this.saveFile(fileInfo, path.toFile(), generateThumbnails);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public List<IniFileInfo> saveFile(@Nonnull Path... paths) {
        return this.saveFile(true, paths);
    }

    @Override
    public List<IniFileInfo> saveFile(boolean generateThumbnails, @Nonnull Path... paths) {
        List<IniFileInfo> list = Lists.newArrayListWithCapacity(paths.length);
        for (Path path : paths) {
            list.add(this.saveFile(path.toFile(), generateThumbnails));
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public IniFileInfo saveFile(@Nonnull MultipartFile file) {
        return this.saveFile(new IniFileInfo(), file, false);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public IniFileInfo saveFile(@Nonnull MultipartFile file, boolean generateThumbnails) {
        return this.saveFile(new IniFileInfo(), file, generateThumbnails);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public IniFileInfo saveFile(@Nonnull IniFileInfo fileInfo, @Nonnull MultipartFile file) {
        return this.saveFile(fileInfo, file, false);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public IniFileInfo saveFile(@Nonnull IniFileInfo fileInfo, @Nonnull MultipartFile file, boolean generateThumbnails) {
        fileInfo.setName(StringUtils.defaultIfBlank(file.getOriginalFilename(), file.getName()));
        fileInfo.setMimeType(file.getContentType());
        fileInfo.setFileSize(Long.toString(file.getSize()));
        try {
            InputStream inputStream = file.getInputStream();
            IniFileInfo var7;
            try {
                var7 = this.saveFile(fileInfo, inputStream, generateThumbnails);
            } finally {
                inputStream.close();

            }
            return var7;
        } catch (IOException var19) {
            throw new RuntimeException(var19.getMessage(), var19);
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public List<IniFileInfo> saveFile(@Nonnull MultipartFile... files) {
        return this.saveFile(false, files);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public List<IniFileInfo> saveFile(boolean generateThumbnails, @Nonnull MultipartFile... files) {
        List<IniFileInfo> list = Lists.newArrayListWithCapacity(files.length);
        for (MultipartFile file : files) {
            list.add(this.saveFile(file, generateThumbnails));
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public IniFileInfo saveFile(@Nonnull InputStream stream) {
        return this.saveFile(new IniFileInfo(), stream, false);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public IniFileInfo saveFile(@Nonnull InputStream stream, boolean generateThumbnails) {
        return this.saveFile(new IniFileInfo(), stream, generateThumbnails);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public IniFileInfo saveFile(@Nonnull IniFileInfo fileInfo, @Nonnull InputStream stream) {
        return this.saveFile(fileInfo, stream, false);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public IniFileInfo saveFile(@Nonnull IniFileInfo fileInfo, @Nonnull InputStream stream, boolean generateThumbnails) {
        InputStream inputStream = IOUtils.buffer(stream);
        this.getDefaultIfNull(fileInfo, fileInfo.getId(), fileInfo::setId, (entity) -> Long.toString(IdUtil.getSnowflakeNextId()));
        this.defaultIfNull(fileInfo.getName(), fileInfo::setName, "unknown.tmp");
        this.getDefaultIfNull(fileInfo, fileInfo.getMimeType(), fileInfo::setMimeType, (entity) -> StringUtils.defaultString(FileUtils.getContentType(entity.getName()), "application/octet-stream"));
        this.getDefaultIfNull(fileInfo, fileInfo.getFileSize(), fileInfo::setFileSize, (entity) -> Long.toString(stream.available()));
        this.defaultIfNull(fileInfo.getVersionNo(), fileInfo::setVersionNo, "0");
        this.defaultIfNull(fileInfo.getIsTemp(), fileInfo::setIsTemp, "0");
        this.defaultIfNull(fileInfo.getIsEncrypt(), fileInfo::setIsEncrypt, "0");
        this.defaultIfNull(fileInfo.getIsCompress(), fileInfo::setIsCompress, "0");
        this.getDefaultIfNull(fileInfo, fileInfo.getPath(), fileInfo::setPath, (entity) -> DateUtils.datePath() + "/" + String.format("%d.%s", Long.parseLong(fileInfo.getId()), "1".equals(fileInfo.getIsCompress()) ? "gz" : FilenameUtils.getExtension(fileInfo.getName())));
        if (generateThumbnails && this.fileProperties.getThumbnails().isEnable()) {
            try {
                IniFileInfo thumbnailsFileInfo = this.generateThumbnails(fileInfo, inputStream);
                if (Objects.nonNull(thumbnailsFileInfo)) {
                    fileInfo.setThumbnailId(thumbnailsFileInfo.getId());
                }
            } catch (IOException var12) {
                throw new RuntimeException(var12.getMessage(), var12);
            }
        }

        DigestInputStream digestInputStream = null;
        if (this.fileProperties.isSha256() && StringUtils.isBlank(fileInfo.getSha256sum())) {
            inputStream = digestInputStream = new DigestInputStream(inputStream, DigestUtils.getSha256Digest());
        }
        if ("1".equals(fileInfo.getIsCompress()) && this.fileProperties.isCompress()) {
            try {
                inputStream = this.compress(inputStream);
            } catch (IOException var11) {
                throw new RuntimeException(var11.getMessage(), var11);
            }
        }
        if ("1".equals(fileInfo.getIsEncrypt()) && this.fileProperties.getEncrypt().isEnable()) {
            try {
                inputStream = this.encrypt(inputStream);
            } catch (InvalidKeyException var10) {
                throw new RuntimeException(var10.getMessage(), var10);
            }
        }
        IniFileStorageService iniFileStorageService = this.fileStorageServices.orderedStream().findFirst().orElseThrow(() -> new RuntimeException("没有可用的文件存储服务"));
        try {
            iniFileStorageService.saveFileStream(fileInfo, inputStream);
        } catch (Exception var9) {
            throw new RuntimeException(var9.getMessage(), var9);
        }
        if (Objects.nonNull(digestInputStream)) {
            fileInfo.setSha256sum(Hex.encodeHexString(digestInputStream.getMessageDigest().digest()));
        }
        super.save(fileInfo);
        return fileInfo;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public List<IniFileInfo> saveFile(@Nonnull InputStream... streams) {
        return this.saveFile(false, streams);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public List<IniFileInfo> saveFile(boolean generateThumbnails, @Nonnull InputStream... streams) {
        List<IniFileInfo> list = Lists.newArrayListWithCapacity(streams.length);
        for (InputStream stream : streams) {
            list.add(this.saveFile(stream, generateThumbnails));
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteFile(@Nonnull String fileId) {
        this.deleteFile(fileId, false);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteFile(@Nonnull String fileId, boolean thumbnails) {
        IniFileInfo fileInfo = this.getById(fileId, thumbnails);
        if (Objects.nonNull(fileInfo)) {
            this.deleteFile(fileInfo);
        }

    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteFile(@Nonnull IniFileInfo fileInfo) {
        this.removeById(fileInfo.getId());
        this.fileStorageServices.orderedStream().forEach((service) -> {
            try {
                service.deleteFile(fileInfo);
            } catch (Exception var3) {
                throw new RuntimeException(var3.getMessage(), var3);
            }

        });
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteFile(@Nonnull String... fileIds) {
        this.deleteFile(false, fileIds);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteFile(boolean thumbnails, @Nonnull String... fileIds) {
        for (String fileId : fileIds) {
            this.deleteFile(fileId, thumbnails);
        }

    }

    @Override
    public Optional<File> getFile(@Nonnull String fileId) {
        return this.getFile(fileId, false);
    }

    @Override
    public Optional<File> getFile(@Nonnull String fileId, boolean thumbnails) {
        Optional<IniFileInfo> var10000 = Optional.ofNullable(this.getById(fileId, thumbnails));
        this.getClass();
        return var10000.flatMap(this::getFile);
    }

    @Override
    public Optional<File> getFile(@Nonnull IniFileInfo fileInfo) {
        return this.getFileStream(fileInfo).map((inputStream) -> {
            File tempFile = new File(this.getTempDirectory(), FilenameUtils.getName(fileInfo.getName()));
            try {
                try (OutputStream outputStream = IOUtils.buffer(FileUtils.openOutputStream(tempFile))) {
                    IOUtils.copy(inputStream, outputStream);
                }
                return tempFile;
            } catch (IOException var17) {
                throw new RuntimeException(var17.getMessage(), var17);
            }
        });
    }

    @Override
    public Optional<InputStream> getFileStream(@Nonnull String fileId) {
        return this.getFileStream(fileId, false);
    }

    @Override
    public Optional<InputStream> getFileStream(@Nonnull String fileId, boolean thumbnails) {
        Optional<IniFileInfo> var10000 = Optional.ofNullable(this.getById(fileId, thumbnails));
        this.getClass();
        return var10000.flatMap(this::getFileStream);
    }

    @Override
    public Optional<InputStream> getFileStream(@Nonnull IniFileInfo fileInfo) {
        return this.fileStorageServices.orderedStream().map((service) -> {
            try {
                AtomicReference<InputStream> inputStreamRef = new AtomicReference(service.getFileStream(fileInfo));
                if (Objects.isNull(inputStreamRef.get())) {
                    return null;
                } else {
                    if ("1".equals(fileInfo.getIsEncrypt()) && this.fileProperties.getEncrypt().isEnable()) {
                        try {
                            inputStreamRef.set(this.decrypt(inputStreamRef.get()));
                        } catch (InvalidKeyException var6) {
                            throw new RuntimeException(var6.getMessage(), var6);
                        }
                    }
                    if ("1".equals(fileInfo.getIsCompress()) && this.fileProperties.isCompress()) {
                        try {
                            inputStreamRef.set(this.decompress(inputStreamRef.get()));
                        } catch (IOException var5) {
                            throw new RuntimeException(var5.getMessage(), var5);
                        }
                    }
                    return inputStreamRef.get();
                }
            } catch (Exception var7) {
                throw new RuntimeException(var7.getMessage(), var7);
            }
        }).findFirst();
    }

    @Override
    public File zip(@Nonnull String... fileIds) {
        return this.zip(null, false, fileIds);
    }

    @Override
    public File zip(boolean thumbnails, @Nonnull String... fileIds) {
        return this.zip(null, thumbnails, fileIds);
    }

    @Override
    public File zip(@Nullable String fileName, @Nonnull String... fileIds) {
        return this.zip(fileName, false, fileIds);
    }

    @Override
    public File zip(@Nullable String fileName, boolean thumbnails, @Nonnull String... fileIds) {
        File file = StringUtils.isBlank(fileName) ? this.createTempFile(null, ".zip") : new File(this.getTempDirectory(), FilenameUtils.getName(fileName));
        try {
            try (ZipArchiveOutputStream zipOs = new ZipArchiveOutputStream(IOUtils.buffer(FileUtils.openOutputStream(file)))) {
                for (String fileId : fileIds) {
                    IniFileInfo fileInfo = this.getById(fileId, thumbnails);
                    if (!Objects.isNull(fileInfo)) {
                        Optional<InputStream> optional = this.getFileStream(fileInfo);
                        if (optional.isPresent()) {
                            zipOs.putArchiveEntry(new ZipArchiveEntry(FilenameUtils.getName(fileInfo.getName())));
                            IOUtils.copy(optional.get(), zipOs);
                            zipOs.closeArchiveEntry();
                        }
                    }
                }
                zipOs.finish();
                return file;
            }
        } catch (IOException var24) {
            throw new RuntimeException(var24.getMessage(), var24);
        }
    }

    @Override
    public void download(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull String fileId) {
        this.download(request, response, null, false, fileId);
    }

    @Override
    public void download(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull String fileId, boolean thumbnails) {
        this.download(request, response, null, thumbnails, fileId);
    }

    @Override
    public void download(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nullable String fileName, @Nonnull String fileId) {
        this.download(request, response, fileName, false, fileId);
    }

    @Override
    public void download(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nullable String fileName, boolean thumbnails, @Nonnull String fileId) {
        this.responseFile(response, fileName, fileId, thumbnails, "attachment");
    }

    @Override
    public void download(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull String... fileIds) {
        this.download(request, response, null, false, fileIds);
    }

    @Override
    public void download(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, boolean thumbnails, @Nonnull String... fileIds) {
        this.download(request, response, null, thumbnails, fileIds);
    }

    @Override
    public void download(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nullable String fileName, @Nonnull String... fileIds) {
        this.download(request, response, fileName, false, fileIds);
    }

    @Override
    public void download(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nullable String fileName, boolean thumbnails, @Nonnull String... fileIds) {
        File zipFile = this.zip(fileName, thumbnails, fileIds);
        try {
            try (InputStream inputStream = IOUtils.buffer(FileUtils.openInputStream(zipFile))) {
                response.setStatus(200);
                response.setContentType("application/zip");
                response.setContentLengthLong(zipFile.length());
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.setHeader("Content-Disposition", ContentDisposition.builder("form-data").name("attachment").filename(zipFile.getName(), StandardCharsets.UTF_8).build().toString());
                OutputStream outputStream = IOUtils.buffer(response.getOutputStream());
                IOUtils.copy(inputStream, outputStream);
                outputStream.flush();
            }
        } catch (IOException var28) {
            throw new RuntimeException(var28.getMessage(), var28);
        } finally {
            FileUtils.deleteQuietly(zipFile);
        }

    }

    @Override
    public void preview(@Nonnull HttpServletResponse response, @Nonnull String fileId) {
        this.preview(response, false, fileId);
    }

    @Override
    public void preview(@Nonnull HttpServletResponse response, boolean thumbnails, @Nonnull String fileId) {
        this.responseFile(response, null, fileId, thumbnails, "inline");
    }

    @Override
    @Nullable
    public IniFileInfo getById(@Nonnull Serializable id, boolean thumbnails) {
        return Database.getOne(Wrappers.lambdaQuery(IniFileInfo.class).eq(thumbnails, IniFileInfo::getThumbnailId, id).eq(!thumbnails, IniFileInfo::getId, id));
    }

    @Override
    public List<IniFileInfo> listByIds(@Nonnull Iterable<? extends Serializable> idList, boolean thumbnails) {
        return Database.list(Wrappers.lambdaQuery(IniFileInfo.class).in(thumbnails, IniFileInfo::getThumbnailId, idList).in(!thumbnails, IniFileInfo::getId, idList));
    }

    @Override
    public File getTempDirectory() {
        return this.fileProperties.getTempDirectory();
    }

    @Override
    public File createTempFile(@Nullable String prefix, @Nullable String suffix, FileAttribute<?>... attrs) {
        try {
            return Files.createTempFile(this.getTempDirectory().toPath(), prefix, suffix, attrs).toFile();
        } catch (IOException var5) {
            throw new RuntimeException(var5.getMessage(), var5);
        }
    }

    @Override
    public File createTempDirectory(@Nullable String prefix, FileAttribute<?>... attrs) {
        try {
            return Files.createTempDirectory(this.getTempDirectory().toPath(), prefix, attrs).toFile();
        } catch (IOException var4) {
            throw new RuntimeException(var4.getMessage(), var4);
        }
    }

    @Override
    @Nullable
    @Transactional(rollbackFor = {Exception.class})
    public IniFileInfo generateThumbnails(@Nonnull IniFileInfo fileInfo, InputStream inputStream) throws IOException {
        if (!inputStream.markSupported()) {
            inputStream = IOUtils.buffer(inputStream);
        }
        inputStream.mark(2147483647);
        String formatName;
        String mimetype;
        BufferedImage image;
        try {
            ImageInputStream iis = ImageIO.createImageInputStream(inputStream);
            Throwable var8 = null;
            try {
                Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
                ImageReader imageReader;
                if (!imageReaders.hasNext()) {
                    return null;
                }
                imageReader = imageReaders.next();
                imageReader.setInput(iis, true, true);
                try {
                    formatName = imageReader.getFormatName();
                    mimetype = ArrayUtils.get(imageReader.getOriginatingProvider().getMIMETypes(), 0);
                    image = imageReader.read(0, imageReader.getDefaultReadParam());
                } finally {
                    imageReader.dispose();
                }
            } catch (Throwable var50) {
                var8 = var50;
                throw var50;
            } finally {
                if (iis != null) {
                    if (var8 != null) {
                        try {
                            iis.close();
                        } catch (Throwable var47) {
                            var8.addSuppressed(var47);
                        }
                    } else {
                        iis.close();
                    }
                }
            }
        } finally {
            inputStream.reset();
            inputStream.mark(0);
        }

        String var53 = FilenameUtils.getExtension(fileInfo.getName());
        if (StringUtils.isBlank(var53) && StringUtils.isNotBlank(formatName)) {
            fileInfo.setName(fileInfo.getName() + FilenameUtils.EXTENSION_SEPARATOR_STR + formatName);
        }

        if (StringUtils.isNotBlank(mimetype) && !StringUtils.equalsIgnoreCase("application/octet-stream", mimetype)) {
            fileInfo.setMimeType(mimetype);
        }

        if (Objects.isNull(image)) {
            return null;
        } else {
            File thumbnails = this.createTempFile("thumbnails-", FilenameUtils.EXTENSION_SEPARATOR_STR + StringUtils.defaultIfBlank(FilenameUtils.getExtension(fileInfo.getName()), formatName));
            IniFileInfo var56;
            try {
                IniFileInfo thumbnailsFileInfo = new IniFileInfo();
                thumbnailsFileInfo.setName(fileInfo.getName());
                thumbnailsFileInfo.setPath(fileInfo.getPath());
                thumbnailsFileInfo.setVersionId(fileInfo.getVersionId());
                thumbnailsFileInfo.setVersionNo(fileInfo.getVersionNo());
                thumbnailsFileInfo.setIsCompress(fileInfo.getIsCompress());
                thumbnailsFileInfo.setIsEncrypt(fileInfo.getIsEncrypt());
                this.createThumbnails(image, thumbnails);
                this.saveFile(thumbnailsFileInfo, thumbnails, false);
                var56 = thumbnailsFileInfo;
            } finally {
                FileUtils.deleteQuietly(thumbnails);
            }
            return var56;
        }
    }

    @Override
    public void createThumbnails(BufferedImage image, File thumbnails) throws IOException {
        Builder<BufferedImage> builder = Thumbnails.of(image);
        FileProperties.FileThumbnailsProperties properties = this.fileProperties.getThumbnails();
        Integer width = properties.getWidth();
        if (Objects.nonNull(width)) {
            builder.width(width);
        }

        Integer height = properties.getHeight();
        if (Objects.nonNull(height)) {
            builder.height(height);
        }

        Boolean keepAspectRatio = properties.getKeepAspectRatio();
        if (Objects.nonNull(keepAspectRatio)) {
            builder.keepAspectRatio(keepAspectRatio);
        }

        builder.toFile(thumbnails);
    }

    @Override
    public InputStream compress(InputStream inputStream) throws IOException {
        return IOUtil.gzip(inputStream);
    }

    @Override
    public InputStream encrypt(InputStream inputStream) throws InvalidKeyException {
        FileProperties.FileEncryptProperties encryptProperties = this.fileProperties.getEncrypt();
        Cipher cipher = SecureUtil.createCipher(encryptProperties.getAlgorithm().getValue());
        cipher.init(1, KeyUtil.generateKey(encryptProperties.getAlgorithm().getValue(), SecureUtil.decode(encryptProperties.getKey())));
        return new CipherInputStream(inputStream, cipher);
    }

    @Override
    public InputStream decompress(InputStream inputStream) throws IOException {
        return IOUtil.gunzip(inputStream);
    }

    @Override
    public InputStream decrypt(InputStream inputStream) throws InvalidKeyException, IOException {
        FileProperties.FileEncryptProperties encryptProperties = this.fileProperties.getEncrypt();
        Cipher cipher = SecureUtil.createCipher(encryptProperties.getAlgorithm().getValue());
        cipher.init(2, KeyUtil.generateKey(encryptProperties.getAlgorithm().getValue(), SecureUtil.decode(encryptProperties.getKey())));
        return IOUtil.pipe(inputStream, (o) -> new CipherOutputStream(o, cipher));
    }

    private void responseFile(@Nonnull HttpServletResponse response, @Nullable String fileName, @Nonnull String fileId, boolean thumbnails, String contentDispositionName) {
        IniFileInfo fileInfo = this.getById(fileId, thumbnails);
        if (Objects.isNull(fileInfo)) {
            response.setStatus(404);
        } else {
            Optional<InputStream> optional = this.getFileStream(fileInfo);
            if (!optional.isPresent()) {
                response.setStatus(404);
            } else {
                try {
                    try (InputStream inputStream = optional.get()) {
                        response.setStatus(200);
                        response.setContentType(fileInfo.getMimeType());
                        response.setContentLengthLong(Long.parseLong(fileInfo.getFileSize()));
                        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                        response.setHeader("Content-Disposition", ContentDisposition.builder("form-data").name(contentDispositionName).filename(StringUtils.defaultIfBlank(fileName, fileInfo.getName()), StandardCharsets.UTF_8).build().toString());
                        OutputStream outputStream = IOUtils.buffer(response.getOutputStream());
                        IOUtils.copy(inputStream, outputStream);
                        outputStream.flush();
                    }
                } catch (IOException var21) {
                    throw new RuntimeException(var21.getMessage(), var21);
                }
            }
        }
    }

    private <V> void defaultIfNull(V value, Consumer<V> setter, V defaultValue) {
        if (Objects.isNull(value)) {
            setter.accept(defaultValue);
        }

    }

    private <V> void getDefaultIfNull(IniFileInfo fileInfo, V value, Consumer<V> setter, IniFileInfoServiceImpl.CallFunction<V> defaultValueGetter) {
        if (Objects.isNull(value)) {
            try {
                V defaultValue = defaultValueGetter.apply(fileInfo);
                setter.accept(defaultValue);
            } catch (Exception var6) {
                throw new RuntimeException(var6.getMessage(), var6);
            }
        }

    }

    @FunctionalInterface
    private interface CallFunction<V> {
        V apply(IniFileInfo fileInfo) throws Exception;
    }

}
