# Spring Boot 启动参数

 **springboot启动参数** 

某些情况下，我们需要在spring boot服务启动的时候才指定一些参数，这个时候spring boot启动参数就派上用场了。

 **IDEA启动增加参数** 

这个一般在本地调试的时候使用

![](images/2v2-f27e3e6612698bec3e7292d21d553d0d_r.jpg)

两种方式选择其一

```powershell
-Dserver.port=8082
--server.port=8082
```

 **控制台启动添加参数** 

一般运维配合shell脚本使用

-   第一种

```text
java -jar -Dspring.profiles.active=test -Dserver.port=8081 app.jar
```

-   第二种

```text
java -jar app.jar --spring.profiles.active=test --server.port=8081 
```

 **spring boot配置加载顺序** 

  

-   命令行参数。所有的配置都可以在命令行上进行指定；
-   来自java:comp/env的JNDI属性；
-   Java系统属性（System.getProperties()）；
-   操作系统环境变量 ；
-   jar包外部的application-{profile}.properties或application.yml(带spring.profile)配置文件
-   jar包内部的application-{profile}.properties或application.yml(带spring.profile)配置文件 再来加载不带profile
-   jar包外部的application.properties或application.yml(不带spring.profile)配置文件
-   jar包内部的application.properties或application.yml(不带spring.profile)配置文件
-   @Configuration注解类上的@PropertySource

  

更加详细的配置加载顺序可以参考官网文档：[https://docs.spring.io/spring-boot/docs/2.1.11.RELEASE/reference/html/boot-features-external-config.html](https://docs.spring.io/spring-boot/docs/2.1.11.RELEASE/reference/html/boot-features-external-config.html)

  

原文地址：[Spring boot 启动参数](https://zhuanlan.zhihu.com/p/645729926) 


