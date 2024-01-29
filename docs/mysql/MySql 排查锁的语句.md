# MySql 排查锁的命令

## show processlist

### show processlist 的作用

show processlist 是显示用户正在运行的线程，processlist 是 information\_schema 中的一个表。下面这条语句与 show processlist 作用一样

```bash
select * from information_schema.processlist;
```

该语句查询到结果如下图所示

![](images/2v2-9345f95b8248416537772af33cf3eea4_r.jpg)

### processlist 中的字段含义

-   Id: 就是这个线程的唯一标识，当我们发现这个线程有问题的时候，可以通过 kill 命令，加上这个Id值将这个线程杀掉。前面我们说了show processlist 显示的信息时来自information\_schema.processlist 表，所以这个Id就是这个表的主键。
-   User: 就是指启动这个线程的用户。
-   Host: 记录了发送请求的客户端的 IP 和 端口号。通过这些信息在排查问题的时候，我们可以定位到是哪个客户端的哪个进程发送的请求。
-   DB: 当前执行的命令是在哪一个数据库上。如果没有指定数据库，则该值为 NULL 。
-   Command: 是指此刻该线程正在执行的命令。这个很复杂，下面单独解释
-   Time: 表示该线程处于当前状态的时间。
-   State: 线程的状态，和 Command 对应，下面单独解释。
-   Info: 一般记录的是线程执行的语句。默认只显示前100个字符，也就是你看到的语句可能是截断了的，要看全部信息，需要使用 show full processlist。

### 关于processlist 常用的查询语句

```bash
# 按客户端 IP 分组，看哪个客户端的链接数最多
select client_ip, count(client_ip) as client_num
from (select substring_index(host, ':', 1) as client_ip from information_schema.processlist) as connect_info
group by client_ip
order by client_num desc;

# 查看正在执行的线程，并按 Time 倒排序，看看有没有执行时间特别长的线程
select *
from information_schema.processlist
where Command != 'Sleep'
order by Time desc;

# 找出所有执行时间超过 5 分钟的线程，拼凑出 kill 语句，方便后面查杀
select concat('kill ', id, ';')
from information_schema.processlist
where Command != 'Sleep'
  and Time > 300
order by Time desc;
```

## 有助于排查死锁问题的SQL

有些时候通过 show processlist 处理掉有问题的客户端线程后发现表还是被锁着（无法 modify、drop、select）可以尝试使用下面的命令，查询有问题的线程并处理掉。

```text
# 查询正在执行的事务：
SELECT *
FROM information_schema.INNODB_TRX;

# 查看正在锁的事务
SELECT *
FROM INFORMATION_SCHEMA.INNODB_LOCKS;

# 查看等待锁的事务
SELECT *
FROM INFORMATION_SCHEMA.INNODB_LOCK_WAITS;
```

  

原文地址：[记录一些排查MYSQL锁的语句](https://zhuanlan.zhihu.com/p/652163663) 


