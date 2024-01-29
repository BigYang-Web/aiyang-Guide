# Spring Boot Starter 实践


创建和使用自定义的Spring Boot Starter 包括几个主要步骤。下面是一个简单的例子。

## 1. 创建Spring Boot Starter 项目

首先，创建一个Maven或Gradle项目，作为你的Spring Boot Starter。项目结构如下：

```text
my-spring-boot-starter
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── mystarter
│   │   │               └── MyStarterAutoConfiguration.java
│   │   └── resources
│   │       └── META-INF
│   │           └── spring.factories
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── mystarter
│                       └── MyStarterAutoConfigurationTest.java
└── pom.xml
```

## 2. 编写自动配置类

在 `MyStarterAutoConfiguration.java` 中编写自动配置类：

```java
@Configuration
@EnableConfigurationProperties(MyStarterProperties.class)
public class MyStarterAutoConfiguration {

    @Autowired
    private MyStarterProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public MyService myService() {
        return new MyService(properties.getMessage());
    }
}
```

## 3. 添加配置属性类

  
创建一个 `MyStarterProperties.java` 用于配置属性：

```java
@ConfigurationProperties(prefix = "mystarter")
public class MyStarterProperties {

    private String message = "Hello from My Starter";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
```

## 4. 配置文件

在 `src/main/resources` 目录下创建 `application.properties` 文件，用于设置自定义 Starter 的属性：

```properties
mystarter.message=Custom Message
```

## 5. `spring.factories` 文件

在 `src/main/resources/META-INF` 目录下创建 `spring.factories` 文件，指定自动配置类：

```text
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.example.mystarter.MyStarterAutoConfiguration
```

## 6. 打包和发布

将自定义 Starter 打包，并发布到Maven仓库或其他仓库。

## 7. 在其他项目中使用

在另一个Spring Boot项目的 `pom.xml` 文件中添加对自定义 Starter 的依赖：

```xml
<dependencies>
    <dependency>
        <groupId>com.example</groupId>
        <artifactId>my-spring-boot-starter</artifactId>
        <version>1.0.0</version>
    </dependency>
    <!-- 其他依赖 -->
</dependencies>
```

## 8. 使用自定义 Starter

在其他项目的代码中，你可以直接注入 `MyService` 类，它会根据自定义 Starter 的配置进行初始化：

```java
@Service
public class MyServiceUser {

    private final MyService myService;

    @Autowired
    public MyServiceUser(MyService myService) {
        this.myService = myService;
    }

    public void doSomething() {
        System.out.println(myService.getMessage());
    }
}
```

这样，当 `MyServiceUser` 被创建时，它会自动注入一个根据自定义 Starter 配置初始化的 `MyService` 实例。  
  
希望这个简单的例子能够帮助你创建和使用自定义的Spring Boot Starter。

  

原文地址：[Spring boot starter 实践](https://zhuanlan.zhihu.com/p/674073323) 


