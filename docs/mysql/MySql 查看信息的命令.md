# MySql 查看内部信息的命令

```text
# 查看当前账户下的数据库
show databases ;

# 查看某个数据库中的表
show tables from starcharge_saas;

# 查看数据库中表及其状态
show table status from starcharge_saas;

# 查看表中的字段信息
show columns from t_manager;
desc t_manager;

# 列出字段及详情
show full columns from t_manager;

# 列出表索引
show index from t_manager;
```

 **show full columns from t\_manager 本人经常使用这个命令配合IDEA编写数据字典相关的文档，下面是导出的结果** 

![](images/2v2-5ff620b9bd2ddc90387568c07f9564b0_r.jpg)

![](images/3v2-29337c30376ab812dd85d00f4c70862f_r.jpg)

  

原文地址：[记录一写查看MYSQL信息的命令](https://zhuanlan.zhihu.com/p/647936359) 


