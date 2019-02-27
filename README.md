# SmoothNLP
[![GitHub release](https://img.shields.io/badge/Version-0.1-green.svg)](https://github.com/zhangruinan/SmoothNLP/releases)
****	
|Author|VC|
|---|---
|E-mail|ruinan.zhang@icloud.com|
****

<!-- TOC -->

- [SmoothNLP](#smoothnlp)
    - [Installation](#installation)
        - [Python](#python)
        - [Java Server](#java-server)
    - [情感模型 Sentiment Analysis](#情感模型-sentiment-analysis)
        - [情感标注数据生成](#情感标注数据生成)
            - [半监督打标数据生成](#半监督打标数据生成)
            - [BinarizedTree 训练数据数据处理](#binarizedtree-训练数据数据处理)
        - [RNTN Model 模型训练](#rntn-model-模型训练)
    - [NER Normalization 数字与金额的识别](#ner-normalization-数字与金额的识别)
        - [Money 金额识别](#money-金额识别)
        - [Numerical 数字识别](#numerical-数字识别)
    - [CoreNLP NER 训练](https://github.com/zhangruinan/SmoothNLP/tree/master/ner#corenlp%E6%A1%86%E6%9E%B6)

<!-- /TOC -->

-----------

## Installation
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
``` 
------------

## 情感模型 Sentiment Analysis 
### 情感标注数据生成
#### 半监督打标数据生成
```python
from smoothnlp.treebanks import initNLP,lines2labeled_lines
initNLP("http://127.0.0.1",port=9000) ## 启动一个CoreNLP的Server
sample_liens = ['今天天气不错', "我心情也不错"]
lines2labeled_lines(sample_liens,"train_raw.txt")  ## 利用现有模型先标注，并写入到文件
```
输出文件地址可以参照[这里](https://github.com/zhangruinan/SmoothNLP/blob/master/smoothnlp/examples/sample_out.txt)

#### BinarizedTree 训练数据数据处理
```python
from smoothnlp.treebanks import binarize_labeled_data
binarize_labeled_data("train_raw.txt","train_ready.txt")
```
BinarizedTree 数据格式如下：
```text
(3 (3 今天) (2 (2 天气) (3 不错)))
(2 (2 (2 我) (2 心情)) (3 (2 也) (3 不错)))
```



**模型训练数据 Training data format**
```angular2html
3 今天 天气 不错
3 今天
2 天气
3 不错

2 我 心情 也 不错
2 我 心情
2 我
2 心情
3 也 不错
2 也
3 不错
```

**模型训练数据 (TreeBanks)**
```angular2html
(3, (3, '今天'), (2, (2, '天气'), (3, '不错')))
(2, (2, (2, '我'), (2, '心情')), (3, (2, '也'), (3, '不错')))
```

### RNTN Model 模型训练

```shell
java -mx8g -cp corenlp-chinese-smoothnlp-0.1-with-dependencies.jar edu.stanford.nlp.sentiment.SentimentTraining -numHid 20 -trainPath train_ready.txt -train -model model.ser.gz
```

## NER Normalization 数字与金额的识别
### Money 金额识别
```python
import smoothnlp
print(smoothnlp.money_recognize("价值两千五百元"))
print(smoothnlp.money_recognize("我新买的iphone原价要八百刀"))
```

```shell
[{'normalized_entity': '元2500', 'type': 'MONEY', 'start': 2, 'end': 7}]
[{'normalized_entity': '$800', 'type': 'MONEY', 'start': 13, 'end': 16}]
```
### Numerical 数字识别
```python
smoothnlp.number_recognize("后宫佳丽有三千")
```

```shell
[{'normalized_entity': '3000', 'type': 'NUMBER', 'start': 5, 'end': 7}]
```
