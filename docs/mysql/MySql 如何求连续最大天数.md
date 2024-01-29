# MySql 如何求连续最大天数



在 MySQL 中，如果你有一个包含日期和某种事件的表，你想要找到连续最大天数的记录，可以通过使用窗口函数和变量来实现。以下是一个基本的示例：

  

假设你有一个表名为 \`events\`，包含日期字段 \`event\_date\` 和某种事件字段 \`event\_type\`，你可以使用以下 SQL 查询来找到连续最大天数的记录：

```mysql
SELECT
  MAX(consecutive_days) AS max_consecutive_days
FROM (
  SELECT
    event_type,
    COUNT(*) AS consecutive_days
  FROM (
    SELECT
      event_type,
      event_date,
      event_date - INTERVAL ROW_NUMBER() OVER (PARTITION BY event_type ORDER BY event_date) DAY AS grp
    FROM
      events
  ) AS tmp
  GROUP BY
    event_type,
    grp
) AS result;
```

  

这个查询的逻辑如下：

1. 使用窗口函数 \`ROW\_NUMBER()\` 对每个 \`event\_type\` 分组，并按 \`event\_date\` 排序，为每个事件分配一个行号。

2. 使用 \`event\_date - INTERVAL ROW\_NUMBER() DAY\` 计算每个事件与其行号的日期差，生成一个分组标识 \`grp\`，使得连续的日期具有相同的 \`grp\` 值。

3. 对结果进行两层聚合：

\- 外层聚合按 \`event\_type\` 分组。

\- 内层聚合按 \`event\_type\` 和 \`grp\` 分组，计算每个分组的连续天数。

4. 最终，选择连续天数最大值作为结果。

  

请注意，这个查询假设你的 \`event\_date\` 是日期类型的列。如果不是，你可能需要进行适当的转换。此外，这个查询可能需要根据实际情况进行调整，以满足你的表结构和数据模型。

  

原文地址：[MySQL如何求连续最大天数](https://zhuanlan.zhihu.com/p/673391930) 


