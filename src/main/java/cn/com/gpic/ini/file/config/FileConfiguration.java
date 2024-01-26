package cn.com.gpic.ini.file.config;

import cn.com.gpic.ini.file.util.FileProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lzk&yjj
 */
@Configuration
@EnableConfigurationProperties({FileProperties.class})
public class FileConfiguration {
    public FileConfiguration() {
    }
}
