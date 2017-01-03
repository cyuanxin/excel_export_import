# 特性

* xml配置
* 导出类型、字段类型、标题可配
* 解析错误信息反馈
* 基于反射的自动映射

# xml配置

## excel导出(resources/export/exportconfig.xml)

* exportConfig: 导出文件名称
* exportType: 导出类型(0:excel,1:csv)
* title: 输出title
* alias: 输出对应的list的property或者map的key

## excel导入(resources/export/config.xml)

* startRowNo: 从n行开始算起，从0开始
* number: 第m列
* key: 解析存储的map对应的key
* cellType: 数字或者java类型名称     转换的数据类型(0 或 Int),(1 或 Float),(2 或 String),(3 或 Date),(4 或 BigDecimal),(5 或 Double)
* nullble: 是否可为空 0可为空 1 不允许
* 返回结果MapResult<Map>: List<Map>内容，resMsg解析结果(如果空，表示数据行通过，有不合法内容则返回相应的错误信息)
* map map.get(LINE_NUM_KEY)可得到所在的行数, map.get(isLineLegal)可得到该行是否合法

# 功能

* 使用xml进行数据类型，数据开始行，导出数据格式，进行配置，方便维护，代码简洁
* 对各种所预设的数据类型进行格式校验，并且返回错误的行数和信息
* 导出时，把list or map 转化成 对应的excel或者csv 
* 读取excel，转换成相应的map或者其他内容(其他内容暂时还未开发)。目前只有map。

# demo

* test->main方法，执行import和export方法

