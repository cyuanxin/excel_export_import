# excel_export_import
## 简介
### 为什么开发
* 做系统的时候，经常需要对文件导出以及excel的导入。我们最熟悉的是便是poi。但是poi本身只是提供了对excel的处理，没有具体到每个字段

### 存在的问题
#### excel导出

* 比如字段A，B,就需要写个代码处理AB，下次业务说换成C，需要修改大量代码
* 如果不要导出excel，导成csv。

####  excel导入

* 同导出一样，经常字段更改
* 无法控制excel的输入格式，就算你跟用户强调了输入框必需什么格式，实际操作仍然有不同格式，就需要开发进行代码对不同格式进行操作

### 功能

* 使用xml进行数据类型，数据开始行，导出数据格式，进行配置，方便维护，代码简洁
* 对各种所预设的数据类型进行格式校验，并且返回错误的行数和信息
* 导出时，把list or map 转化成 对应的excel或者csv 
* 读取excel，转换成相应的map或者其他内容(其他内容暂时还未开发)。目前只有map。

### 使用

#### Example

* test->main方法，执行import和export方法

#### xml配置

##### excel导出(resources/export/exportconfig.xml)

* <fileName>exportConfig</fileName> 导出文件名称
* <exportType>0</exportType> 导出类型(0:excel,1:csv)
* <title>int</title> 输出第一行的title
* <alias>index</alias> 输出对应的list的property或者map的key

##### excel导入(resources/export/config.xml)

* <startRowNo>n</startRowNo>从n行开始算起，从0开始
* <number>m</number>第m列
* <cellType>数字或者java类型名称</cellType>转换的数据类型(0 或 Int),(1 或 Float),(2 或 String),(3 或 Date),(4 或 BigDecimal),(5 或 Double)
* <nullble>0</nullble>是否可为空 0可为空 1 不允许

* 返回结果MapResult<Map>: List<Map>内容，resMsg解析结果(如果空，表示数据行通过，有不合法内容则返回相应的错误信息)
* map map.get(LINE_NUM_KEY)可得到所在的行数, map.get(isLineLegal)可得到该行是否合法