# MySql 操作时间的命令


```mysql
# 获取当时时间
select now();
select current_date;
select current_time;
select current_timestamp;

# 将时间格式化为 YYYY-mm-dd HH:ii:ss 格式
select date_format(now(),'%Y-%m-%d %H:%i:%s');


# 获取当时时间戳（10位长度）
SELECT UNIX_TIMESTAMP();

#  将时间戳格式化为 YYYY-mm-dd HH:ii:ss 格式日期（默认）
SELECT FROM_UNIXTIME(UNIX_TIMESTAMP());


# 将时间戳格式化日期（指定日期格式化格式）
SELECT FROM_UNIXTIME(UNIX_TIMESTAMP(),'%Y-%m-%d %H:%i:%s');

# 处理13位的时间戳转换
SELECT FROM_UNIXTIME(1627311955999/1000,'%Y-%m-%d %H:%i:%s');


# 获取时间差的函数
period_diff()    datediff(date1,date2)      timediff(time1,time2)

# 日期加减函数
date_sub()
date_add()
adddate()
addtime()
period_add(P,N)
```

  

原文地址：[记录一些MYSQL操作时间的命令](https://zhuanlan.zhihu.com/p/648003900) 


