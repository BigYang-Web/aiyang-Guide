# Spring Boot 线程池用起来

 **前言** 

最近公司架构组来了几位大佬，带头做起了服务治理。经过压测发现我负责的服务存在慢接口，没得办法开始改吧。这次文章先记录下试用线程池优化接口响应时长的。

 **先了解下场景** 

前辈写的一个导出接口，当时数据量并不大，直接做成了同步查询的+导出的。随着数据量越来越大，现在的导出接口越来越慢，到了不得不优化的境地

 **实现方案** 

后端提供两个接口

1.  发起导出：创建导出任务，返回任务ID，使用spring boot 线程池异步导出，导出结果入库
2.  查询导出结果：根据任务ID，实时查询导出结果

前端流程：发起导出任务，返回任务ID，通过任务ID轮询导出结果。

 **简单聊下线程池** 

因为用到了spring boot线程池，先了解下线程池的概念：

线程池就是管理一系列线程的资源池，其提供了一种限制和管理线程资源的方式。

Java提供了Executor框架，来帮助我们管理线程池，线程池实现类 ThreadPoolExecutor 是 Executor 框架最核心的类，下面是该类的构造方法。

```java
       /**
     * 用给定的初始参数创建一个新的ThreadPoolExecutor。
     */
    public ThreadPoolExecutor(int corePoolSize,//线程池的核心线程数量
                              int maximumPoolSize,//线程池的最大线程数
                              long keepAliveTime,//当线程数大于核心线程数时，多余的空闲线程存活的最长时间
                              TimeUnit unit,//时间单位
                              BlockingQueue<Runnable> workQueue,//任务队列，用来储存等待执行任务的队列
                              ThreadFactory threadFactory,//线程工厂，用来创建线程，一般默认即可
                              RejectedExecutionHandler handler//拒绝策略，当提交的任务过多而不能及时处理时，我们可以定制策略来处理任务
                               ) {
        if (corePoolSize < 0 ||
            maximumPoolSize <= 0 ||
            maximumPoolSize < corePoolSize ||
            keepAliveTime < 0)
            throw new IllegalArgumentException();
        if (workQueue == null || threadFactory == null || handler == null)
            throw new NullPointerException();
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        this.keepAliveTime = unit.toNanos(keepAliveTime);
        this.threadFactory = threadFactory;
        this.handler = handler;
    }
```

 **创建spring boot 线程池** 

先创建一个线程池的配置，让Spring Boot加载，用来定义如何创建一个ThreadPoolTaskExecutor，要使用@Configuration和@EnableAsync这两个注解，表示这是个配置类，并且是线程池的配置类。

```java
@Configuration
@EnableAsync
public class ExecutorConfig {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorConfig.class);

    @Value("${async.executor.thread.core_pool_size}")
    private int corePoolSize;
    @Value("${async.executor.thread.max_pool_size}")
    private int maxPoolSize;
    @Value("${async.executor.thread.queue_capacity}")
    private int queueCapacity;
    @Value("${async.executor.thread.name.prefix}")
    private String namePrefix;

    @Bean(name = "asyncServiceExecutor")
    public Executor asyncServiceExecutor() {
        logger.info("start asyncServiceExecutor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(corePoolSize);
        //配置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //配置队列大小
        executor.setQueueCapacity(queueCapacity);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(namePrefix);

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}
```

 **使用spring boot 线程池** 

我这边提供了两种方式调用

1.  提供一个持有 Executor 的 ExcelProduceActuator 组件，其他服务类调用该组件

```java
@Component
@RequiredArgsConstructor
public class ExcelProduceActuator {

    @Resource(name = "excelDownloadExecutor")
    private Executor executor;

    private final RedisLock redisLock;
    private final LocalMessageDecorator messageDecorator;
    private final RemoteMessageDecorator kafkaDecorator;

    /**
     * 导出Excel
     *
     * @param query        查询参数
     * @param remoteCaller 远程调用信息
     * @param redisLocker  锁信息
     */
    public <P, R> void asyncProduceExcel(ExcelProducer<R, P> producer, P query, RemoteCaller remoteCaller, RedisLocker redisLocker) {
        boolean hasData = producer.hasData(query);
        if (!hasData){
            throw new ServiceException("没有数据哦");
        }
        Boolean lockResult = redisLock.tryLock(redisLocker);
        if (Objects.equals(lockResult, Boolean.FALSE)) {
            throw new ServiceException("已经有相同的导出任务在进行");
        }
        executor.execute(() -> this.asyncProduceExcel(producer, query, remoteCaller));
    }
}
```

1.  直接使用spring bootd的异步注解

```text
/**
 * 导出Excel
 *
 * @param query        查询参数
 * @param remoteCaller 远程调用信息
 */
@Async("excelDownloadExecutor")
public <P, R> void asyncProduceExcel(ExcelProducer<R, P> producer, P query, RemoteCaller remoteCaller) {
    this.produceExcel(producer, query, remoteCaller);
}
```

看情况使用对应的方法即可。

  

原文地址：[spring 线程池用起来](https://zhuanlan.zhihu.com/p/645323862) 


