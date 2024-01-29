# MySql 语句的分类

 **SQL语句的分类简介** 

SQL语句共分为四大类：

数据查询语句DQL、数据操纵语句DML、数据定义语句DDL、数据控制语句DCL

###  **数据查询语言DQL** 

数据查询语句DQL的基本结构是由SELECT子句、FROM子句、WHRER子句组成

SELECT <字段名表> FROM <表或视图名> WHERE <查询条件>

###  **数据操纵语言DML** 

1.  插入：INSERT
2.  更新：UPDATE
3.  删除：DELETE

###  **数据定义语言DDL** 

数据定义语言DDL用来创建数据库中的各种对象-----表、视图、索引、同义词、聚簇等如

CREATE TABLE、VIEW、INDEX、SYN、CLUSTER-----------表 视图 索引 同义词 簇

 **DDL的所需权限** 

需要排他访问权限

CREATE、ALTER、DROP和PURGE：创建、修改、删除和清空方案（schema）对象

RENAME：修改对象名称 TRUNCATE：截断表

无需排他访问权限

GRANT和REVOKE：授予及收回权限、角色

ANALYZE：分析表、索引或簇的信息

COMMENT：为表、视图、字段等对象创建注释

###  **数据控制语言DCL** 

数据控制语言DCL用来授予或回收访问数据库的某种特权，并控制

数据库操纵事务发生的时间及效果，对数据库实行监视等。如：

1.  GRANT：授权。
2.  ROLLBACK ［WORK］ TO \[SAVEPOINT\]：回退到某一点。回滚---ROLLBACK 回滚命令使数据库状态回到上次最后提交的状态。其格式为：SQL>ROLLBACK;

3. COMMIT ［WORK］：提交。

在数据库的插入、删除和修改操作时，只有当事务在提交到数据 库时才算完成。

在事务提交前，只有当前会话看到所做的更改（事务隔离等级）其它用户或会话在提交完成后才可以看到。

事务控制（Transaction Control）

COMMIT、ROLLBACK、SAVEPOINT

会话控制（Session Control）

alter session set nls\_date\_format='yyyy-mm-dd hh24:mi:ss';

系统控制（System Control） ALTER SYSTEM

###  **提交数据的类型** 

(1) 显式提交

用COMMIT命令直接完成的提交为显式提交。其格式为：

SQL>COMMIT；

(2) 隐式提交

用SQL命令间接完成的提交为隐式提交。这些命令是：

ALTER，AUDIT，COMMENT，CONNECT，CREATE，DISCONNECT，DROP，

EXIT，GRANT，NOAUDIT，QUIT，REVOKE，RENAME。

(3) 自动提交

若把AUTOCOMMIT设置为ON，则在插入、修改、删除语句执行后，

系统将自动进行提交，这就是自动提交。其格式为：

SQL>SET AUTOCOMMIT ON；

  

原文地址：[记录一下MYSQL语句的分类](https://zhuanlan.zhihu.com/p/648184541) 


