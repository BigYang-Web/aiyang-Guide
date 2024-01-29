# MySql 操作字符串的命令


```text
# left函数：从左开始截取字符串
# 格式：left（str, length）
# 说明：left（被截取字段，截取长度）
select left(name,1) from t_manager;

# right函数：从右开始截取字符串
# 格式：right（str, length）
# 说明：right（被截取字段，截取长度）
select right(name,1) from t_manager;


# substring 函数：截取字符串
# 格式一：substring（str, pos）说明：substring（被截取字段，从第几位开始截取）
# 格式二：substring（str, pos, length） 说明：substring（被截取字段，从第几位开始截取，截取长度）

select substring(name,1) from t_manager;
select substring(name,1,2) from t_manager;

# substring_index函数：拆分字符串
# 格式：substring_index（str,delim,count）
# 说明：substring_index（被截取字段，关键字，关键字出现的次数）
# 注：如果关键字出现的次数是负数，则是从后倒数，到字符串结束

select substring_index('ali/ten/bai','/',2);
select substring_index('ali/ten/bai','/',1);
select substring_index('ali/ten/bai','/',-1);
```

  

原文地址：[记录一些MYSQL操作字符串的命令](https://zhuanlan.zhihu.com/p/647952984) 


