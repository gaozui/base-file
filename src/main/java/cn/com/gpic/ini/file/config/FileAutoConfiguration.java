package cn.com.gpic.ini.file.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author lzk&yjj
 */
@ComponentScan({"cn.com.gpic.ini.file"})
@MapperScan("cn.com.gpic.ini.file.mapper")
public class FileAutoConfiguration {
    public FileAutoConfiguration() {
    }
}
