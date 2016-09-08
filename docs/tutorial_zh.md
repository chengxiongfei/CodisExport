#导出模版配置

##模版是json格式，每个表的配置分为4个部分

 - 表名
 - 包含的所有的列
 - 每个列对应的过滤条件
 - 表的过滤条件
 
 ##样例
```json
{
  "footfall-Callerloc" : {
    "header":"area_code,province_id",
    "conditions":{
      "area_code":{
        "like":"111|888",
        "sign":"&&"
      }
    },
    "where":"area_code||province_id"
  }
}
```
 
 - 第一行为表名，例如`footfall-Callerloc`
 
 - 第二行`header`后面加上所有参与计算的列名字，用逗号分隔
 
 - 第三行`conditions`定义每个列的过滤条件目前支持的运算符为
 
 | 符号      |  值的类型  |
 | :-------:| :-----:|
 | ==       | 数字 |
 | !=       | 数字 |
 | <        | 数字 |
 | <=       | 数字 |
 | \>       | 数字 | 
 | \>=      | 数字 |
 | like     | 符合Java语法规范的正则表达式 |
 | eq       | 字符串 |
 | ne       | 字符串 |
 
 在`conditions`里面还要配置`sign`含义是以上条件之间是与`&&`还是或`||`目前只能定义一种关系，如果不写，默认是`&&`
 
 - 最后一行的`where`条件，是指在`conditions`里面定义了过滤条件的列之间是与`&&`还是或`||`的关系，可以是多个与`&&`和或`||`，但是不支持有括号，默认
 按照运算符的优先级规则进行运算。
 
 ##总结
 以上是对一个表的配置，在tables.json的配置文件里面可以配置多个表

   
   
 
 