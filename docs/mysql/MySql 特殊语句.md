# MySql 特殊语句

不存在插入，存在就更新

```mysql
INSERT INTO table (id, name, age) values (1, 'yourname', 18) 
ON DUPLICATE KEY UPDATE name='yourname', age=18;
```

从表A获取数据插入表B

```text
SELECT column_name(s)
INTO new_table_name [IN externaldatabase]
FROM old_tablename
```

  

原文地址：[MySL特殊语句](https://zhuanlan.zhihu.com/p/673294068) 


