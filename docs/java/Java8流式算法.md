# 1.过滤　　

## 1.1 filter

我们希望过滤赛选处所有学校是清华大学的user：

```java
System.out.println("学校是清华大学的user");

List<User> userList1 = list.stream().filter(user -> "清华大学".equals(user.getSchool())).collect(Collectors.toList());

userList1.forEach(user -> System.out.print(user.name + '、'));
```

## 1.2 distinct

去重，我们希望获取所有user的年龄（年龄不重复）

```java
System.out.println("所有user的年龄集合");

List<Integer> userAgeList = list.stream().map(User::getAge).distinct().collect(Collectors.toList());

System.out.println("userAgeList = " + userAgeList);
```

## 1.3 limit 

返回前n个元素的流，当集合的长度小于n时，则返回所有集合。

如获取年龄是偶数的前2名user：

```java
System.out.println("年龄是偶数的前两位user");

List<User> userList3 = list.stream().filter(user -> user.getAge() % 2 == 0).limit(2).collect(Collectors.toList());

userList3.forEach(user -> System.out.print(user.name + '、'));
```

## 1.4 sorted

排序，如现在我想将所有user按照age从大到小排序：

```java
System.out.println("按年龄从大到小排序");

List<User> userList4 = list.stream().sorted((s1,s2) -> s2.age - s1.age).collect(Collectors.toList());

userList4.forEach(user -> System.out.print(user.name + '、'));
```

## 1.5 skip

跳过n个元素后再输出

如输出list集合跳过前两个元素后的list

```java
System.out.println("跳过前面两个user的其他所有user");

List<User> userList5 = list.stream().skip(2).collect(Collectors.toList());

userList5.forEach(user -> System.out.print(user.name + '、'));
```

# 2 映射

## 2.1 map

就是讲user这个几个精简为某个字段的集合

如我现在想知道学校是清华大学的所有学生的姓名：

```java　　
System.out.println("学校是清华大学的user的名字");

List<String> userList6 = list.stream().filter(user -> "清华大学".equals(user.school)).map(User::getName).collect(Collectors.toList());

userList6.forEach(user -> System.out.print(user + '、'));
```

除了上面这类基础的map，java8还提供了

```java
mapToDouble(ToDoubleFunction<? super T> mapper)，

mapToInt(ToIntFunction<? super T> mapper)，

mapToLong(ToLongFunction<? super T> mapper)，
```

这些映射分别返回对应类型的流，java8为这些流设定了一些特殊的操作，比如查询学校是清华大学的user的年龄总和：

```java
System.out.println("学校是清华大学的user的年龄总和");

int userList7 = list.stream().filter(user -> "清华大学".equals(user.school)).mapToInt(User::getAge).sum();

System.out.println( "学校是清华大学的user的年龄总和为： "+userList7);
```

## 1.2 flatMap

flatMap与map的区别在于 flatMap是将一个流中的每个值都转成一个个流，然后再将这些流扁平化成为一个流 。

举例说明，假设我们有一个字符串数组String[] strs = {"hello", "world"};，我们希望输出构成这一数组的所有非重复字符，那么我们用map和flatMap 实现如下：

```java
String[] strings = {"Hello", "World"};

List l11 = Arrays.stream(strings).map(str -> str.split("")).map(str2->Arrays.stream(str2)).distinct().collect(Collectors.toList());

List l2 = Arrays.asList(strings).stream().map(s -> s.split("")).flatMap(Arrays::stream).distinct().collect(Collectors.toList());

System.out.println(l11.toString());System.out.println(l2.toString());　　
```

由上我们可以看到使用map并不能实现我们现在想要的结果，而flatMap是可以的。

这是因为在执行map操作以后，我们得到是一个包含多个字符串（构成一个字符串的字符数组）的流，此时执行distinct操作是基于在这些字符串数组之间的对比，所以达不到我们希望的目的；

flatMap将由map映射得到的Stream<String[]>，转换成由各个字符串数组映射成的流Stream<String>，再将这些小的流扁平化成为一个由所有字符串构成的大流Steam<String>，从而能够达到我们的目的。

# 3 查找

## 3.1 allMatch

用于检测是否全部都满足指定的参数行为，如果全部满足则返回true，例如我们判断是否所有的user年龄都大于9岁，实现如下：

```java
System.out.println("判断是否所有user的年龄都大于9岁");

Boolean b = list.stream().allMatch(user -> user.age >9);

System.out.println(b);
```



## 3.2 anyMatch

anyMatch则是检测是否存在一个或多个满足指定的参数行为，如果满足则返回true，例如判断是否有user的年龄大于15岁，实现如下：

```java
System.out.println("判断是否有user的年龄是大于15岁");

Boolean bo = list.stream().anyMatch(user -> user.age >15);System.out.println(bo);
```

## 3.3 noneMatch　　

noneMatch用于检测是否不存在满足指定行为的元素，如果不存在则返回true，例如判断是否不存在年龄是15岁的user，实现如下：

```java
System.out.println("判断是否不存在年龄是15岁的user");

Boolean boo = list.stream().noneMatch(user -> user.age == 15);System.out.println(boo);
```

## 3.4 findFirst

findFirst用于返回满足条件的第一个元素，比如返回年龄大于12岁的user中的第一个，实现如下:

```java
System.out.println("返回年龄大于12岁的user中的第一个");

Optional<User> first = list.stream().filter(u -> u.age > 10).findFirst();

User user = first.get();System.out.println(user.toString());
```



## 3.5 findAny

findAny相对于findFirst的区别在于，findAny不一定返回第一个，而是返回任意一个，比如返回年龄大于12岁的user中的任意一个：

```java
System.out.println("返回年龄大于12岁的user中的任意一个");

Optional<User> anyOne = list.stream().filter(u -> u.age > 10).findAny();

User user2 = anyOne.get();System.out.println(user2.toString());
```



# 4 归约

## 4.1 reduce

现在我的目标不是返回一个新的集合，而是希望对经过参数化操作后的集合进行进一步的运算，那么我们可用对集合实施归约操作。java8的流式处理提供了reduce方法来达到这一目的。

比如我现在要查出学校是清华大学的所有user的年龄之和。

//前面用到的方法

```java
Integer ages = list.stream().filter(student -> "清华大学".equals(student.school)).mapToInt(User::getAge).sum();

System.out.println(ages);

System.out.println("归约 - - 》 start ");

Integer ages2 = list.stream().filter(student -> "清华大学".equals(student.school)).map(User::getAge).reduce(0,(a,c)->a+c);

Integer ages3 = list.stream().filter(student -> "清华大学".equals(student.school)).map(User::getAge).reduce(0,Integer::sum);

Integer ages4 = list.stream().filter(student -> "清华大学".equals(student.school)).map(User::getAge).reduce(Integer::sum).get();

System.out.println(ages2);

System.out.println(ages3);

System.out.println(ages4);

System.out.println("归约 - - 》 end ");
```



# 5 收集

前面利用collect(Collectors.toList())是一个简单的收集操作，是对处理结果的封装，对应的还有toSet、toMap，以满足我们对于结果组织的需求。

这些方法均来自于java.util.stream.Collectors，我们可以称之为收集器。

收集器也提供了相应的归约操作，但是与reduce在内部实现上是有区别的，收集器更加适用于可变容器上的归约操作，这些收集器广义上均基于Collectors.reducing()实现。

## 5.1 counting （计算个数）

如我现在计算user的总人数，实现如下：

```java
System.out.println("user的总人数");

long COUNT = list.stream().count();//简化版本

long COUNT2 = list.stream().collect(Collectors.counting());//原始版本

System.out.println(COUNT);

System.out.println(COUNT2);
```



## 5.2 maxBy、minBy（计算最大值和最小值）

如我现在计算user的年龄最大值和最小值：

```java
System.out.println("user的年龄最大值和最小值");

Integer maxAge =list.stream().collect(Collectors.maxBy((s1, s2) -> s1.getAge() - s2.getAge())).get().age;

Integer maxAge2 = list.stream().collect(Collectors.maxBy(Comparator.comparing(User::getAge))).get().age;

Integer minAge = list.stream().collect(Collectors.minBy((S1,S2) -> S1.getAge()- S2.getAge())).get().age;

Integer minAge2 = list.stream().collect(Collectors.minBy(Comparator.comparing(User::getAge))).get().age;

System.out.println("maxAge = " + maxAge);System.out.println("maxAge2 = " + maxAge2);System.out.println("minAge = " + minAge);System.out.println("minAge2 = " + minAge2);
```



## 5.3 summingInt、summingLong、summingDouble（总和）

如计算user的年龄总和：

```java
System.out.println("user的年龄总和");

Integer sumAge =list.stream().collect(Collectors.summingInt(User::getAge));

System.out.println("sumAge = " + sumAge);
```



## 5.4 averageInt、averageLong、averageDouble（平均值）

如计算user的年龄平均值：

```java
System.out.println("user的年龄平均值");

double averageAge = list.stream().collect(Collectors.averagingDouble(User::getAge));

System.out.println("averageAge = " + averageAge);
```



## 5.5 summarizingInt、summarizingLong、summarizingDouble

一次性查询元素个数、总和、最大值、最小值和平均值

```java
System.out.println("一次性得到元素个数、总和、均值、最大值、最小值");

long l1 = System.currentTimeMillis();

IntSummaryStatistics summaryStatistics = list.stream().collect(Collectors.summarizingInt(User::getAge));

long l111 = System.currentTimeMillis();

System.out.println("计算这5个值消耗时间为" + (l111-l1));

System.out.println("summaryStatistics = " + summaryStatistics);
```



## 5.6 joining 字符串拼接

如输出所有user的名字，用“，”隔开

```java
System.out.println("字符串拼接");

String names = list.stream().map(User::getName).collect(Collectors.joining(","));

System.out.println("names = " + names);
```



## 5.7 groupingBy（分组）

如将user根据学校分组、先按学校分再按年龄分、每个大学的user人数、每个大学不同年龄的人数：

```java
System.out.println("分组");

Map<String, List<User>> collect1 = list.stream().collect(Collectors.groupingBy(User::getSchool));

Map<String, Map<Integer, Long>> collect2 = list.stream().collect(Collectors.groupingBy(User::getSchool, Collectors.groupingBy(User::getAge, Collectors.counting())));

Map<String, Map<Integer, Map<String, Long>>> collect4 = list.stream().collect(Collectors.groupingBy(User::getSchool, Collectors.groupingBy(User::getAge, Collectors.groupingBy(User::getName,Collectors.counting()))));

Map<String, Long> collect3 = list.stream().collect(Collectors.groupingBy(User::getSchool, Collectors.counting()));

System.out.println("collect1 = " + collect1);System.out.println("collect2 = " + collect2);System.out.println("collect3 = " + collect3);System.out.println("collect4 = " + collect4);
```



## 5.8 partitioningBy（分区）

分区可以看做是分组的一种特殊情况，在分区中key只有两种情况：true或false，目的是将待分区集合按照条件一分为二，java8的流式处理利用ollectors.partitioningBy()方法实现分区。

如按照是否是清华大学的user将左右user分为两个部分：

```java
System.out.println("分区");

Map<Boolean, List<User>> collect5 = list.stream().collect(Collectors.partitioningBy(user1 -> "清华大学".equals(user1.school)));

System.out.println("collect5 = " + collect5);
```

