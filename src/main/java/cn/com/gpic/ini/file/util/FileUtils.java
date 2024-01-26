package cn.com.gpic.ini.file.util;


import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Objects;

/**
 * @author lzk&yjj
 */
public class FileUtils extends org.apache.commons.io.FileUtils {
    private static final MimetypesFileTypeMap FILE_TYPE_MAP = new MimetypesFileTypeMap();
    private static final FileNameMap FILE_NAME_MAP = URLConnection.getFileNameMap();

    private FileUtils() {
        throw new AssertionError();
    }

    @Nullable
    public static String getContentType(File file) {
        return getContentType(file, file.getName());
    }

    @Nullable
    public static String getContentType(@Nonnull File file, @Nonnull String fileName) {
        String contentType = null;
        try {
            contentType = Files.probeContentType(file.toPath());
        } catch (Exception ignored) {
        }
        if (Objects.isNull(contentType)) {
            contentType = FILE_TYPE_MAP.getContentType(file);
        }
        return Objects.nonNull(contentType) ? contentType : getContentType(fileName);
    }

    @Nullable
    public static String getContentType(@Nonnull String fileName) {
        String contentType = URLConnection.guessContentTypeFromName(fileName);
        if (Objects.isNull(contentType)) {
            contentType = FILE_NAME_MAP.getContentTypeFor(fileName);
        }
        if (Objects.isNull(contentType) && Objects.nonNull(getSession())) {
            contentType = getSession().getServletContext().getMimeType(fileName);
        }
        return Objects.nonNull(contentType) ? contentType : null;
    }

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取session
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 获取web请求属性
     */
    public static ServletRequestAttributes getRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

}
