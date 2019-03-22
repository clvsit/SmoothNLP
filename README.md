# SmoothNLP
[![GitHub release](https://img.shields.io/badge/Version-0.1-green.svg)](https://github.com/zhangruinan/SmoothNLP/releases)
****	
|Author|VC|
|---|---
|E-mail|ruinan.zhang@icloud.com|
****


<!-- ----------- -->

## Installation
### Python
请注意使用`python3`安装smoothnlp项目
```shell
pip3 install git+https://github.com/zhangruinan/SmoothNLP.git
```

### Java Maven-Project
**SmoothNLP**项目的主要功能都在Java中有实现, 打包好的Jar文件会在[Release页面]定期更新, 或者在提供的[maven](https://github.com/zhangruinan/SmoothNLP/tree/master/smoothnlp_maven)项目代码中, 直接编译即可
```
git clone https://github.com/zhangruinan/SmoothNLP.git
cd smoothnlp_maven
mvn clean package
```
编译好的Jar文件会在 `smoothnlp_maven/target/smoothnlp-*.jar`

------------

### Usage 调用 
#### Initialize 启动
目前在*0.1*版本中, python的功能支持, 还是通过`jpype`将thread添加到一个运行的jvm上,原理类似于 `pyhanlp` 与 `HanLP`, 在*0.2*版本中, 将支持 smoothnlp-*.jar 通过脚本从Release中自动下载的功能. 
```python
import smoothnlp
smoothnlp.initJVMConnection("/smoothnlp_maven/target/smoothnlp-0.1-jar-with-dependencies.jar")  
```

#### 1. NER 命名实体识别
SmoothNLP继承了将识别出来的实体进行normalize的功能, 主要支持对象有: 日期(Date),金额(Money),数量(Number)
```python
import json
ner = smoothnlp.simple.ner()
json.loads(ner.analyze("在2019年3月八号,我买了个价值五十元的十个苹果"))
```
output 输出:
```
[{'charOffsets': '(1,10)',
  'entityType': 'DATE',
  'tokens': '[2019年-2, 3月-3, 八号-4]',
  'normalizedEntityTag': '2019-03-08',
  'text': '2019年3月八号'},
 {'charOffsets': '(17,20)',
  'entityType': 'MONEY',
  'tokens': '[五十-11, 元-12]',
  'normalizedEntityTag': '元50',
  'text': '五十元'},
 {'charOffsets': '(21,22)',
  'entityType': 'NUMBER',
  'tokens': '[十-14]',
  'normalizedEntityTag': '10',
  'text': '十'}]
```

#### 2. Sentiment 情感分析
```python
sentiment= smoothnlp.simple.sentiment()
sentiment.analyze("今天天气不错")
```
output 输出:
```
'Positive'
```


<!-- ## Installation
### Python 
```shell
pip3 install git+https://github.com/zhangruinan/SmoothNLP.git
```
Please notice, the python package is only a convenient wrapper on CoreNLP's server in jre. 
Therefore, for many functionalities, you may start the Java Server first. 

### Java Server
You may download the latest jar file from [here](https://github.com/zhangruinan/SmoothNLP/releases)
. After download the archive file, unzip it and run it in jvm.

Jar文件[下载地址](https://github.com/zhangruinan/SmoothNLP/releases),下载解压后可运行Server
```shell
unzip corenlp-chinese-smoothnlp-*.zip
java -jar corenlp-chinese-smoothnlp-*-with-dependencies.jar 
```
Please notice, the corenlp server runs on port 9000 on default, you may change settings based on 
[official document](https://stanfordnlp.github.io/CoreNLP/corenlp-server.html) by parsing in additional arguments.
For instance:

如需添加额外参数，可参考[官方文档](https://stanfordnlp.github.io/CoreNLP/corenlp-server.html),下面是一个额外参数调用的例子：
```shell
java -cp corenlp-chinese-smoothnlp-0.1-with-dependencies.jar com.smoothnlp.nlp.RESTServer -port 9001
```  -->


### 自训练Annotator-模型配置
在完成自训练模型后,须将对应的模型地址参数指向模型文件地址. 关于CoreNLP官方支持的Annotator相关Argument具体文档, 
可以看[这里](https://stanfordnlp.github.io/CoreNLP/annotators.html). 

SmoothNLP中实现了3种模型加载方式. 下面将用情感模型(sentiment.model)举例

* 通过编写*.properties* 文件并在启动Server的时候加载 (推荐)
```angular2
java -jar smoothnlp-*-jar-with-dependencies.jar -props nlp.properties
```

* 启动Server时通过传入额外参数实现 (不推荐)
```angular2
java -jar smoothnlp-*-jar-with-dependencies.jar sentiment.model edu/stanford/nlp/models/sentiment/sentiment_model_zh.ser.gz
```

* 在调用Server时传入Annotator相关参数 (推荐,由于CoreNLP相关模型支持Latent加载)
这里举例一个wget在shell里的调用
```
wget --post-data '今天天气不错' 'localhost:9000/?properties={"annotators": "tokenize,sentiment", 
"sentiment.model":"edu/stanford/nlp/models/sentiment/sentiment_model_zh.gz","outputFormat": "json","pipelineLanguage":"zh"}' -O -
```


### TODO
* [ ] 重构HomePage Document, 修改为Java中API, 和其他基础信息
* [ ] python 项目中添加Jpype支持
* [ ] Segment/切词模块的开发