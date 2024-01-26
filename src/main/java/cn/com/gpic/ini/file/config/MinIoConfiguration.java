package cn.com.gpic.ini.file.config;

import cn.com.gpic.ini.file.util.FileProperties;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lzk&yjj
 */
@Configuration
@ConditionalOnClass({MinioClient.class})
@EnableConfigurationProperties({FileProperties.class})
@ConditionalOnProperty(value = {"file.storage.minio-file-storage.enable"}, matchIfMissing = true)
public class MinIoConfiguration {
    private final FileProperties.FileStorageProperties.MinioFileStorageProperties minioFileStorage;

    @Autowired
    MinIoConfiguration(FileProperties fileProperties) {
        this.minioFileStorage = fileProperties.getStorage().getMinioFileStorage();
    }

    @Bean
    @ConditionalOnMissingBean
    MinioClient minioClient() {
        return MinioClient.builder().endpoint(this.minioFileStorage.getEndpoint()).credentials(this.minioFileStorage.getAccessKey(), this.minioFileStorage.getSecretKey()).build();
    }
}
