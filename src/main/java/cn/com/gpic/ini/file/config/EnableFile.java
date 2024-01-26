package cn.com.gpic.ini.file.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author lzk&yjj
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({FileAutoConfiguration.class})
@Documented
@Inherited
public @interface EnableFile {
}
