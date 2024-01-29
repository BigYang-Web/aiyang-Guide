# Spring Boot 启动事件

## Spring boot 启动事件有哪些？

在 Spring Boot 中，有许多启动事件，每个事件都对应着应用程序生命周期中的不同阶段。以下是一些常见的 Spring Boot 启动事件：

1.   **ApplicationStartingEvent：**  在应用程序开始启动时触发，此时还没有开始加载配置和上下文。  
    
2.   **ApplicationEnvironmentPreparedEvent：**  在应用程序环境准备好之后触发，但在创建 ApplicationContext 之前。可以用于动态调整配置。  
    
3.   **ApplicationPreparedEvent：**  在 ApplicationContext 创建完成，但在加载应用程序的 bean 之前触发。可以在这个阶段进行一些自定义的初始化工作。  
    
4.   **ApplicationStartedEvent：**  在刷新 ApplicationContext 之后触发，表示应用程序已经启动完成，此时可以执行一些启动后的操作。  
    
5.   **ApplicationReadyEvent：**  在所有应用程序和命令行运行器 `CommandLineRunner` 执行之前触发，表示应用程序已经准备好接受服务请求。  
    
6.   **ApplicationFailedEvent：**  如果启动时发生异常，则触发此事件。你可以通过监听此事件来处理启动失败的情况。  
    
7.   **ApplicationPreparedEvent：**  在 ApplicationContext 刷新之前触发，表示上下文已准备好，但尚未刷新。  
    
8.   **ApplicationStartingEvent：**  在开始刷新之前，早于 `ApplicationPreparedEvent`。  
    
9.   **ApplicationReadyEvent：**  在 `EmbeddedWebServerInitializedEvent` 之后触发，表示嵌入式 Web 服务器已初始化。  
    
10.   **EmbeddedWebServerInitializedEvent：**  在嵌入式 Web 服务器初始化后触发。  
    

这些事件提供了灵活的扩展点，你可以根据需要选择监听哪些事件，并在事件触发时执行自定义逻辑。

## 使用Spring boot 启动事件

Spring Boot 应用程序启动时，可以通过监听应用程序启动事件来执行特定的操作。在 Spring 中，可以使用 `ApplicationListener` 或者 `@EventListener` 注解来实现这个功能。

1.   **使用 ApplicationListener 接口：** 

```java
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class MyApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // 在应用程序启动完成后执行的逻辑
        System.out.println("Application is ready to use!");
    }
}
```

1.   **使用 @EventListener 注解：** 

```java
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MyEventListener {

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(ApplicationReadyEvent event) {
        // 在应用程序启动完成后执行的逻辑
        System.out.println("Application is ready to use!");
    }
}
```

这两种方式都会在应用程序启动完成后触发，你可以根据需要选择其中一种方式。注意，在使用 `@EventListener` 注解时，监听方法不能有任何参数，但你可以通过 `ApplicationEvent` 类型的参数获取事件的相关信息。

这只是一个简单的示例，你可以根据实际需求执行更复杂的逻辑。

  

原文地址：[Spring boot 启动事件](https://zhuanlan.zhihu.com/p/673632543) 


