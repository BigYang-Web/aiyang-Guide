# Spring Boot BeanPostProcessor 获取服务内部信息

## 先看这里哦

在搭建公司内部服务中间件的时候，不会像业务对接一样，给对方一份接口文档，让对方按照文档接入，这样做格局太低了。

我们往往需要给业务开发人员提供client包，封装好内部逻辑，不对客户端产生过多的侵入性，这才是一个合格的服务中间件。

今天给予Spring boot BeanPostProcessor，提供一种获取服务内部方法的思路给大家

## 思路简析

原理相对比较简单，在spring boot服务的启动过程中，会调用 BeanPostProcessor 的方法。基于这个原理，我们自己实现一个 BeanPostProcessor 在启动过程中获取 拥有我们创建的注解的方法信息。这样就能收集到我们想要的客户端信息。

有什么用呢？如果在client包中我们内置一些 HTTP工具，将服务内部信息上传到中间件的服务端，就可以对client端进行有针对性的操作了。

##  **创建方法注解** 

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ：zhaoyangyang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface SystemService {

    /**
     * 服务编号：建议使用SYS+数字
     *
     * @return 服务编号
     */
    String serviceNumber();

    /**
     * 服务名称：建议纯中文
     *
     * @return 服务名称
     */
    String serviceName();

    /**
     * 创建人：系统服务创建人
     *
     * @return 创建人
     */
    String createUser();

    /**
     * 创建时间：系统服务创建时间
     *
     * @return 创建时间
     */
    String createDateTime();
}
```

##  **创建方法描述bean** 

```java
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author ：zhaoyangyang
 */
@Data
public class SystemServiceInfo {

    /**
     * 服务编号：建议使用SYS+数字
     */
    @Schema(description = "服务编号")
    private String serviceNumber;

    /**
     * 服务名称：建议纯中文
     */
    @Schema(description = "服务名称")
    private String serviceName;

    /**
     * 创建人：系统服务创建人
     */
    @Schema(description = "创建人")
    private String createUser;

    /**
     * 创建时间：系统服务创建时间
     */
    @Schema(description = "创建时间")
    private String createDateTime;

}
```

##  **创建内部方法类** 

```java
import org.springframework.stereotype.Component;

/**
 * @author ：dayang
 */
@Component
public class DemoService {


    @SystemService(serviceNumber = "sys-SysService1", serviceName = "系统服务1", createUser = "SC4924", createDateTime = "2023-01-01")
    public void SysService1() {

    }


    @SystemService(serviceNumber = "sys-SysService2", serviceName = "系统服务2", createUser = "SC4924", createDateTime = "2023-01-01")
    public void SysService2() {

    }
}
```

##  **创建 BeanPostProcessor** 

```java
import com.starcharge.commons.core.util.CoreTool;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dayang
 **/
@Component
public class DemoPostProcessor implements BeanPostProcessor {

    protected static final Class<SystemService> SYSTEM_SERVICE_CLASS = SystemService.class;

    private final Map<String, SystemServiceInfo> systemServiceInfoMap = new HashMap<>();

    public Map<String, SystemServiceInfo> getSystemServiceInfoMap() {
        return systemServiceInfoMap;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Class<?> clazz = bean.getClass();
        if (AopUtils.isAopProxy(bean)) {
            clazz = AopUtils.getTargetClass(bean);
        }
        //只关注本类声明的方法
        Method[] methods = clazz.getDeclaredMethods();
        if (CoreTool.isEmpty(methods)) {
            return bean;
        }

        for (Method method : methods) {
            SystemService mc = AnnotationUtils.getAnnotation(method, SYSTEM_SERVICE_CLASS);
            if (mc == null) {
                continue;
            }

            SystemServiceInfo info = new SystemServiceInfo();
            info.setServiceNumber(mc.serviceNumber());
            info.setServiceName(mc.serviceName());
            info.setCreateUser(mc.createDateTime());
            info.setCreateDateTime(mc.createDateTime());

            systemServiceInfoMap.put(mc.serviceNumber(), info);
        }
        return bean;
    }


}
```

##  **创建接口展示** 

```text
import com.starcharge.stubuser.dto.governor.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author ：dayang
 */
@RestController
public class DemoController {

    @Autowired
    private DemoPostProcessor demoPostProcessor;


    @Operation(summary = "系统服务信息")
    @GetMapping("/system/service/collection")
    public BaseResponse<Collection<SystemServiceInfo>> test() {
        Collection<SystemServiceInfo> collection = demoPostProcessor.getSystemServiceInfoMap().values();
        return new BaseResponse<>(collection);
    }
}
```

  

原文地址：[Spring boot BeanPostProcessor 获取服务内部信息](https://zhuanlan.zhihu.com/p/666649827) 


