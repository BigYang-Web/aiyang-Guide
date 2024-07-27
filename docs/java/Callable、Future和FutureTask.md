Callable、Future和FutureTask

**一、Callable 与 Runnable**

java.lang.Runnable吧，它是一个接口，在它里面只声明了一个run()方法：

```java
public interface Runnable {
		public abstract void run();
}
```

由于run()方法返回值为void类型，所以在执行完任务之后无法返回任何结果。

Callable位于java.util.concurrent包下，它也是一个接口，在它里面也只声明了一个方法，只不过这个方法叫做call()：

```java
public interface Callable<V> {

    /**
     * \* Computes a result, or throws an exception if unable to do so.
     * <p>
     * <p>
     * <p>
     * \* @return computed result
     * <p>
     * \* @throws Exception if unable to compute a result
     */

    V call() throws Exception;
}
```



可以看到，这是一个泛型接口，该接口声明了一个名称为call()的方法，同时这个方法可以有返回值V，也可以抛出异常。call()方法返回的类型就是传递进来的V类型。

那么怎么使用Callable呢？一般情况下是配合ExecutorService来使用的，在ExecutorService接口中声明了若干个submit方法的重载版本：

```java
<T> Future<T> submit(Callable<T> task);
<T> Future<T> submit(Runnable task, T result);
Future<?> submit(Runnable task);
```



第一个方法：submit提交一个实现Callable接口的任务，并且返回封装了异步计算结果的Future。

第二个方法：submit提交一个实现Runnable接口的任务，并且指定了在调用Future的get方法时返回的result对象。

第三个方法：submit提交一个实现Runnable接口的任务，并且返回封装了异步计算结果的Future。

因此我们只要创建好我们的线程对象（实现Callable接口或者Runnable接口），然后通过上面3个方法提交给线程池去执行即可。

**二、Future**

  Future就是对于具体的Runnable或者Callable任务的执行结果进行取消、查询是否完成、获取结果。必要时可以通过get方法获取执行结果，该方法会阻塞直到任务返回结果。

  Future<V>接口是用来获取异步计算结果的，说白了就是对具体的Runnable或者Callable对象任务执行的结果进行获取(get())，取消(cancel())，判断是否完成等操作。我们看看Future接口的源码：

```java
public interface Future<V> {
    boolean cancel(boolean mayInterruptIfRunning);
    boolean isCancelled();
    boolean isDone();
    V get() throws InterruptedException, ExecutionException;
    V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;
}
```



在Future接口中声明了5个方法，下面依次解释每个方法的作用：

也就是说Future提供了三种功能：

1）判断任务是否完成；

2）能够中断任务；

3）能够获取任务执行结果。

因为Future只是一个接口，所以是无法直接用来创建对象使用的，因此就有了下面的FutureTask。

**三、FutureTask**

我们先来看一下FutureTask的实现：

public class FutureTask<V> implements RunnableFuture<V>

FutureTask类实现了RunnableFuture接口，我们看一下RunnableFuture接口的实现：

public interface RunnableFuture<V> extends Runnable, Future<V> {

​    void run();

}

可以看出RunnableFuture继承了Runnable接口和Future接口，而FutureTask实现了RunnableFuture接口。所以它既可以作为Runnable被线程执行，又可以作为Future得到Callable的返回值。

**分析：**

FutureTask除了实现了Future接口外还实现了Runnable接口，因此FutureTask也可以直接提交给Executor执行。 当然也可以调用线程直接执行（FutureTask.run()）。接下来我们根据FutureTask.run()的执行时机来分析其所处的3种状态：

（1）未启动，FutureTask.run()方法还没有被执行之前，FutureTask处于未启动状态，当创建一个FutureTask，而且没有执行FutureTask.run()方法前，这个FutureTask也处于未启动状态。

（2）已启动，FutureTask.run()被执行的过程中，FutureTask处于已启动状态。

（3）已完成，FutureTask.run()方法执行完正常结束，或者被取消或者抛出异常而结束，FutureTask都处于完成状态。

**四、使用示例**

通过上面的介绍，我们对Callable，Future，FutureTask都有了比较清晰的了解了，那么它们到底有什么用呢？

我们前面说过通过这样的方式去创建线程的话，最大的好处就是能够返回结果。

加入有这样的场景，我们现在需要计算一个数据，而这个数据的计算比较耗时，而我们后面的程序也要用到这个数据结果，那么这个时Callable岂不是最好的选择？

我们可以开设一个线程去执行计算，而主线程继续做其他事，而后面需要使用到这个数据时，我们再使用Future获取不就可以了吗？下面我们就来编写一个这样的实例。

1、使用Callable+Future获取执行结果

**package com.demo.test;**

**import java.util.concu**rr**ent.Callable;**

**public class Task impl**em**ents Callable<Integer>{**

​    

​    **@Override**

​    **public Integer cal**l(**) throws Exception {**

​        **System.out.pri**nt**ln("子线程在进行计算");**

​        **Thread.sleep(3**00**0);**

​        **int sum = 0;**

​        **for(int i=0;i<**10**0;i++)**

​            **sum += i;**

​        **return sum;**

​    **}**

**}**

**package com.demo.test;**

**import java.util.concu**rr**ent.ExecutionException;**

**import java.util.concu**rr**ent.ExecutorService;**

**import java.util.concu**rr**ent.Executors;**

**import java.util.concu**rr**ent.Future;**

**public class CallableT**es**t {**

​    **public static void** m**ain(String[] args) {**

​        **//创建线程池**

​        **ExecutorServic**e **executor = Executors.newCachedThreadP**oo**l();**

​        **//创建Callable对象**任务  

​        **Task task = ne**w **Task();**

​        **//提交任务并获取执行结果**  

​        **Future<Integer**> **result = executor.submit(task);**

​        **//关闭线程池**  

​        **executor.shutd**ow**n();**

​         

​        **try {**

​            **Thread.sle**ep**(1000);**

​        **} catch (Inter**ru**ptedException e1) {**

​            **e1.printSt**ac**kTrace();**

​        **}**

​         

​        **System.out.pri**nt**ln("主线程在执行任务");**

​         

​        **try {**

​            **if(result.**ge**t()!=null){**  

​                **System**.o**ut.println("task运行结果"+result.get());**

​            **}else{**

​                **System**.o**ut.println("未获取到结果");** 

​            **}**

​        **} catch (Inter**ru**ptedException e) {**

​            **e.printSta**ck**Trace();**

​        **} catch (Execu**ti**onException e) {**

​            **e.printSta**ck**Trace();**

​        **}**

​         

​        **System.out.pri**nt**ln("所有任务执行完毕");**

​    **}**

**}**

**2、使用Callable+FutureTask获取执行结果**

**package com.demo.test;**

**import java.util.concu**rr**ent.ExecutionException;**

**import java.util.concu**rr**ent.ExecutorService;**

**import java.util.concu**rr**ent.Executors;**

**import java.util.concu**rr**ent.FutureTask;**

**public class CallableT**es**t1 {**

​    

​    **public static void** m**ain(String[] args) {**

​        **//第一种方式**

​        **ExecutorServic**e **executor = Executors.newCachedThreadPool();**

​        **Task task = ne**w **Task();**

​        **FutureTask<Int**eg**er> futureTask = new FutureTask<Integer>(task);**

​        **executor.submi**t(**futureTask);**

​        **executor.shutd**ow**n();**

​         

​        **//第二种方式，注意这种方式**和第**一种方式效果是类似的，只不过一个使用的是ExecutorService，一个使用的是Threa**d

**//        Task task =** ne**w Task();**

**//        FutureTask<I**nt**eger> futureTask = new FutureTask<Integer>(task**)**;**

**//        Thread threa**d **= new Thread(futureTask);**

**//        thread.start**()**;**

​         

​        **try {**

​            **Thread.sle**ep**(1000);**

​        **} catch (Inter**ru**ptedException e1) {**

​            **e1.printSt**ac**kTrace();**

​        **}**

​         

​        **System.out.pri**nt**ln("主线程在执行任务");**

​         

​        **try {**

​            **if(futureT**as**k.get()!=null){**  

​                **System**.o**ut.println("task运行结果"+futureTask.get());**

​            **}else{**

​                **System**.o**ut.println("future.get()未获取到结果");** 

​            **}**

​        **} catch (Inter**ru**ptedException e) {**

​            **e.printSta**ck**Trace();**

​        **} catch (Execu**ti**onException e) {**

​            **e.printSta**ck**Trace();**

​        **}**

​         

​        **System.out.pri**nt**ln("所有任务执行完毕");**

​    **}**

**}**

**实现Runnable接口和实现Callable接口的区别：**

1、Runnable是自从java1.1就有了，而Callable是1.5之后才加上去的。

2、Callable规定的方法是call(),Runnable规定的方法是run()。

3、Callable的任务执行后可返回值，而Runnable的任务是不能返回值(是void)。

4、call方法可以抛出异常，run方法不可以。

5、运行Callable任务可以拿到一个Future对象，表示异步计算的结果。它提供了检查计算是否完成的方法，以等待计算的完成，并检索计算的结果。通过Future对象可以了解任务执行情况，可取消任务的执行，还可获取执行结果。

6、加入线程池运行，Runnable使用ExecutorService的execute方法，Callable使用submit方法。