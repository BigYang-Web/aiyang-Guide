# MySql 修改字符集的方式


1.   **查看数据库的字符集方式** 

```mysql
-- database_name 为数据库名称
SHOW CREATE DATABASE database_name;
```

1.   **查看表的字符集方式** 

```text
-- table_name为表的名称
SHOW CREATE TABLE table_name;
```

1.   **查看字段的字符集方式** 

```text
-- column_name为字段名称
SHOW FULL COLUMNS FROM column_name;
```

1.   **修改数据库的字符集方式** 

```text
-- database_name 为数据库名称
-- utf8为目标字符编码
ALTER DATABSE database_name DEFAULT CHARACTER SET utf8;
```

1.   **修改表的字符集方式** 

```text
-- table_name为表的名称
-- utf8为目标字符编码
ALTER TABLE table_name DEFAULT CHARACTER SET utf8;
```

1.   **修改字段的字符集方式** 

```text
-- table_name为表的名称
-- column_name为字段名称
-- varchar(20)为字段的类型
-- utf8为目标字符集
ALTER TABLE table_name CHANGE column_name column_name VARCHAR(20) CHARACTER SET utf8;
```

1.   **同时修改表和表中所有字符类型的字段字符集方式** 

```text
-- 例子：alter table user2 convert to character set utf8 collate utf8_general_ci;
ALTER TABLE tbl_name CONVERT TO CHARACTER SET character_name [COLLATE ...]
```

  

原文地址：[记录一下MYSQL修改字符集的方式](https://zhuanlan.zhihu.com/p/673292861) 


