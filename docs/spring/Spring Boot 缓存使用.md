# Spring Boot 缓存使用

Spring Boot提供了对缓存的支持，通过使用`@EnableCaching`注解开启缓存功能，并通过`@Cacheable`、`@CacheEvict`等注解来配置缓存行为。下面是一个简单的例子。

## 1. 创建一个服务类`UserService`，对其方法进行缓存

```java
@Service
@EnableCaching
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Cacheable("users")
    public User getUserById(Long userId) {
        System.out.println("Fetching user from database: " + userId);
        return userRepository.findById(userId).orElse(null);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void clearUserCache() {
        System.out.println("Clearing user cache");
    }
}
```

在这个例子中，`@EnableCaching`启用了缓存功能，`@Cacheable("users")`表示对`getUserById`方法的结果进行缓存，缓存的名称是"users"。当你调用`getUserById`方法时，如果缓存中存在相应的结果，则直接返回缓存中的值，而不执行实际的方法体；如果缓存中不存在，则执行方法体，并将结果存入缓存。  
`@CacheEvict(value = "users", allEntries = true)`表示在调用`clearUserCache`方法时清空名为"users"的缓存。

## 2. 配置一个缓存管理器

使用`@Bean`注解配置`CacheManager`

```java
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("users");
    }
} 
```

在上述配置中，我们使用了`ConcurrentMapCacheManager`，它在内存中管理缓存。在实际项目中，你可能会选择其他缓存管理器，如EhCache、Caffeine等，具体选择取决于项目需求和性能要求。

## 3. 在其他服务或组件中调用`UserService`的方法

```java
@Service
public class MyService {

    @Autowired
    private UserService userService;

    public void doSomething() {
        // 第一次调用，会执行方法体并将结果缓存
        User user = userService.getUserById(1L);
        System.out.println("User: " + user);

        // 第二次调用，直接从缓存中获取，不执行方法体
        User cachedUser = userService.getUserById(1L);
        System.out.println("Cached User: " + cachedUser);

        // 清空缓存
        userService.clearUserCache();

        // 缓存被清空后，再次调用会执行方法体并将结果缓存
        User newUser = userService.getUserById(1L);
        System.out.println("New User: " + newUser);
    }
}
```

  

原文地址：[Spring boot 缓存使用](https://zhuanlan.zhihu.com/p/674076225) 


