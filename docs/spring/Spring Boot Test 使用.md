# Spring Boot Test 使用

## controller 层测试用例

  
Spring Boot 提供了一些方便的工具来编写测试用例。一般使用JUnit来编写单元测试，可以使用Mockito来模拟依赖项。下面是一个简单的例子。我创建了一个controller类 `UserController`，其中包含一些方法，我们将为其中一个方法编写测试用例。  

### 添加依赖项

  
首先，确保在你的项目中包含了适当的测试依赖，例如JUnit和Mockito。在Maven项目中，可以在 `pom.xml` 文件中添加以下依赖：

```xml
<dependencies>
    <!-- 其他依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>3.12.4</version> <!-- 使用最新版本 -->
        <scope>test</scope>
    </dependency>
</dependencies>
```

### 创建`UserController`

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
```

### 编写测试用例

```java
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testGetUserById() throws Exception {
        // Arrange
        Long userId = 1L;
        User mockUser = new User(userId, "John Doe");
        when(userService.getUserById(userId)).thenReturn(mockUser);

        // Act and Assert
        mockMvc.perform(get("/api/users/{userId}", userId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(userId.intValue())))
               .andExpect(jsonPath("$.name", is("John Doe")));
    }

    // 可以添加更多的测试用例...
}
```

在这个例子中，我们使用了 `@WebMvcTest` 来测试 `UserController`，并使用 `@MockBean` 来模拟 `UserService`。然后，我们编写了一个测试用例来验证 `getUserById` 方法的行为。

## service 层编写测试用例

###   
添加依赖项

同controller层一样

### 创建 `UserService` 类

```java
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
```

### 编写测试用例

```java
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testGetUserById() {
        // Arrange
        Long userId = 1L;
        User mockUser = new User(userId, "John Doe");
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("John Doe", result.getName());
    }

    @Test
    public void testGetUserByIdNotFound() {
        // Arrange
        Long userId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertNull(result);
    }

    // 可以添加更多的测试用例...
}
```

在这个例子中，我们使用了 `@InjectMocks` 来注入 `userService`，并使用 `@Mock` 来模拟 `userRepository`。然后，我们编写了两个测试用例，分别测试 `getUserById` 方法存在用户和不存在用户的情况。

## repository 层编写测试用例

编写Spring Boot Repository的测试用例通常涉及对数据访问层的方法进行测试。在测试中，你可以使用内存数据库（如H2）或者使用实际数据库进行测试。

### 添加依赖项

同controller层一样

### 创建repository类

```java
@Repository
public interface UserRepository {
    
    Optional<User> findById(userId);
    
}
```

### 创建测试类

```java
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindById() {
        // Arrange
        Long userId = 1L;
        User user = new User(userId, "John Doe");
        userRepository.save(user);

        // Act
        Optional<User> result = userRepository.findById(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        assertEquals("John Doe", result.get().getName());
    }

    @Test
    void testFindByNonExistentId() {
        // Arrange
        Long nonExistentUserId = 100L;

        // Act
        Optional<User> result = userRepository.findById(nonExistentUserId);

        // Assert
        assertFalse(result.isPresent());
    }

    // 可以添加更多的测试用例...
}
```

在这个例子中，我们使用了 `@DataJpaTest` 来测试JPA数据访问，Spring Boot会自动配置嵌入式数据库（如H2）供测试使用。测试方法中，我们使用了 `userRepository` 对象，执行了一些简单的数据访问操作，并对结果进行断言。

  

原文地址：[Spring boot Test 使用](https://zhuanlan.zhihu.com/p/674082158) 


