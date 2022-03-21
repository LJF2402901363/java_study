# ES踩坑问题

## 1.mongo中字段值既有float又有integer)导致保存到es时候索引的mapping报错“'illegal_argument_exception', 'mapper [tag.tag.is_exclusive] cannot be changed from type [float] to [long]



解决办法：保存mongo的文档到es之前先将类型为float的字段值转换为int类型值，然后再保存到es中去

## 2.'error': {'type': 'illegal_argument_exception', 'reason': 'Limit of total fields [1000] has been exceeded'}

这是由于索引mapping的默认字段field总数（index.mapping.total_fields.limit）是1000，当索引字段总数超出1000个时候就会报错。解决办法使用put方法修改修改“index.mapping.total_fields.limit”的值：

```json
PUT machine/_settings
{
  "index.mapping.total_fields.limit": 2000
}
```

