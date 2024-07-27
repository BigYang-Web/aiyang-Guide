# javaSpring @Validated 实现自定义校验规则

\`@Validated\` 是Spring Framework中用于启用方法级别的验证的注解。要实现自定义校验规则，需要按照以下步骤进行操作

## 1. 创建自定义注解

创建一个自定义的注解，用于标记需要进行验证的字段或方法。

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomValidation {
    String message() default "Custom validation failed";
}
```

  

## 2. 实现自定义验证逻辑

创建一个验证器类，实现 \`ConstraintValidator\` 接口，用于定义自己的验证逻辑。

```java
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomValidator implements ConstraintValidator<CustomValidation, String> {

    @Override
    public void initialize(CustomValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 自定义验证逻辑，根据需要进行逻辑判断
        return value != null && value.startsWith("custom_prefix");
    }
}
```

  

## 3. 在目标类或方法上使用自定义注解

在需要进行验证的字段、方法或参数上使用自定义的注解。

```java
public class YourClass {
    
    @CustomValidation
    private String customField;

    // 方法级别的验证
    @CustomValidation
    public void yourMethod(@CustomValidation String parameter) {
        // 方法体
    }
}
```

  

## 4. 启用方法级别验证

在Spring配置中启用方法级别验证，确保 \`@Validated\` 注解生效。

```java
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
        validatingListener.addValidator("customValidator", new CustomValidator());
    }
}
```

  

原文地址：[spring @Validated 实现自定义校验规则](https://zhuanlan.zhihu.com/p/676250456) 

