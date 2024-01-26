# base_file

## 📚系统简介

系统模块·文件模块

## 💡依赖引用

        <dependency>
            <groupId>io.github.gaozui</groupId>
            <artifactId>base-file</artifactId>
            <version>1.0.0</version>
        </dependency>

## 🚩相关配置

1. 启动类相关配置

        @SpringBootApplication
        @MapperScan("cn.com.gpic.主项目路径.*.mapper")
        @EnableFile
        public class MpepApplication {
            public static void main(String[] args) {
               SpringApplication.run(MpepApplication.class, args);
            }
        } 

2. yml相关配置：

        third:
          prefix: 主项目表名前缀_

3. mybatis-plus拦截器相关配置：

        @Value("${third.prefix}")
        public String tablePrefix;

        private static final List<String> TABLE_LIST = Arrays.asList(FileConstants.TABLE_FILE);

        @Bean
        public MybatisPlusInterceptor mybatisPlusInterceptor() {
          // 拦截器
          MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
          // 动态表名
          DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
            dynamicTableNameInnerInterceptor.setTableNameHandler((sql, tableName) -> {
              if (TABLE_LIST.contains(tableName)) {
                return tablePrefix + tableName;
              } else {
                return tableName;
              }
          });
          interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
          return interceptor;
        }

## 📝内置功能

1.  文件模块：实现对文件上传、下载的集中管理。

## 🍊目录结构

        main                                            # 主目录
        ├── java                                        # Java代码
        │   └── cn
        │       └── com
        │           └── gpic
        │               └── ini
        │                   └── file                    # 文件模块目类
        └── resources                                   # 资源文件目录
            └── mapper                                  # mapper-xml文件

## 📐主要jar包说明
| 包名                | 内容                  |
|-------------------|---------------------|
| common            | 基础系统-通用模块           |
| minio             | minio文件服务           |

## 🐾更新日志

    2023-06-06 项目初始化