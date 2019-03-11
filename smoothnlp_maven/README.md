# SmoothNLP - A Maven Project
## Maven Project Compilation
```shell
git clone https://github.com/zhangruinan/SmoothNLP.githttps://github.com/zhangruinan/SmoothNLP.git
cd smoothnlp_maven
mvn clean package   ### 依据提供给的pom打包maven项目
```

## Java API
### Simple Utilities
SmoothNLP项目中实现了很多常用功能的简化调用, 所以支持功能都在`com.smoothnlp.nlp.simple`下, 
下面是一些主要功能的演示

### HiveUDF
SmoothNLP中支持的HiveUDF均在com.smoothnlp.nlp.HiveUDF下, 具体代码在[这里](https://github.com/zhangruinan/SmoothNLP/tree/master/smoothnlp_maven/src/main/java/com/smoothnlp/nlp/HiveUDF): 

| UDF 名称 | 描述及解释 | HQL实例 |
|-------| -------- | --------- |
| x      | x       | x        | 

### Todo
* [ ] Doc on current supported java API
* [ ] Add Hive UDF implementations for many NLP-related functions
* [ ] Add Hive UDAF implementations for many NLP-related functions
* [ ] JUnit Test on Existing Modules
