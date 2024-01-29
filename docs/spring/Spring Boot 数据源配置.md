# Spring Boot 数据源配置

[toc]



 **写在最前面** 

前端时间在处理公司老项目改造使用新框架的事情，对于spring数据源配置这块有些疑惑，就翻了一下资料，了解了下spring 数据源配置相关的知识，这里记录一下。

 **默认数据源** 

spring boot 默认支持4种数据源类型，定义在 org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration中

1.  org.apache.tomcat.jdbc.pool.DataSource
2.  com.zaxxer.hikari.HikariDataSource
3.  org.apache.commons.dbcp.BasicDataSource
4.  org.apache.commons.dbcp2.BasicDataSource

  

对于上述四种数据源，在相关类存在classpath时，会通过自动配置生成对应的 DataSource Bean，当存在多种数据源时，会按照优先级生成优先级高的：Tomcat--> Hikari --> Dbcp --> Dbcp2 。

 **添加依赖与配置** 

1. 在Springboot 使用JDBC可直接添加官方提供的 spring-boot-start-jdbc 或者 spring-boot-start-data-jpa 依赖。

```text
 <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>${spring-boot-version}</version>
</parent>
<dependencies>
    <!-- 添加MySQL依赖 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    <!-- 添加JDBC依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jdbc</artifactId>
    </dependency>
</dependencies>
```

2. 在核心配置application.properties或者application.yml文件中添加数据源相关配置。

```text
spring.datasource.type=com.zaxxer.hikari.HikariDataSource
# spring.datasource.type=org.apache.tomcat.jdbc.pool.DataSource
# spring.datasource.type=org.apache.commons.dbcp.BasicDataSource
# spring.datasource.type=org.apache.commons.dbcp2.BasicDataSource
```

 **切换默认数据源** 

1. 方式一 排除其他的数据源依赖，仅保留需要的数据源依赖；

```text
                 <!-- 添加JDBC依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
            <exclusions>
                <!-- 排除Tomcat-JDBC依赖 -->
                <exclusion>
                    <groupId>org.apache.tomcat</groupId>
                    <artifactId>tomcat-jdbc</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <!-- 添加HikariCP依赖 -->
        <dependency>
            <groupId>com.zaxxer</groupId>
            <artifactId>HikariCP</artifactId>
        </dependency>
```

2. 方式二 通过在核心配置中通过spring.datasource.type属性指定数据源的类型；

```text
spring.datasource.type=com.zaxxer.hikari.HikariDataSource
 # spring.datasource.type=org.apache.tomcat.jdbc.pool.DataSource 
# spring.datasource.type=org.apache.commons.dbcp.BasicDataSource
 # spring.datasource.type=org.apache.commons.dbcp2.BasicDataSource
```

 **第三方数据源** 

很多公司会使用三方数据源，这里记录一下三方数据源的配置，以 阿里开源的 Druid为例。

 **添加依赖与配置** 

```text
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>${spring-boot-version}</version>
    </parent>
    <dependencies>
        <!-- 添加MySQL依赖 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <!-- 添加JDBC依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <!-- 添加Druid依赖 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.6</version>
        </dependency>
    </dependencies>
```

 **定义数据源** 

使用注解@Bean 创建一个DataSource Bean并将其纳入到Spring容器中进行管理即可。

```text
@Configuration
public class DataSourceConfig {
 
    @Autowired
    private Environment env;
 
    @Bean
    public DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;
    }
}
```

 **写在最后** 

看过、读过的东西一定要记录下来，强化记忆否则过段时就就会忘记。关于数据源的内容看了很多遍，我还是会时不时的忘记。记录在这里与大家共勉

  

原文地址：[Spring boot 数据源配置](https://zhuanlan.zhihu.com/p/642300380) 


