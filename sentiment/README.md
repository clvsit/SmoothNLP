# Sentiment Study -- SmoothNLP
在所有情感模型方案中, 作者认为CoreNLP提供的模型和方案是效果最高且工程化最好的. 主要分为两点: 
- CoreNLP提出的RNTN模型的训练样本是基于一个[BinarizedAnnotatedTree](https://nlp.stanford.edu/sentiment/treebank.html), 主要还出在于, 
除了每句话有一个情感label以外, 短句和词语也带有label, 
    - 一方面使得在标注工作量不增加太多的情况下,每一个句子有多条(n>=3)条label, 
    - 另外一方面, 基于黑盒的RNN/CNN模型在 `句子` -> `label` 上训练往往能取得比较好的效果, 但是在模型泛化能力上极其有限, 且黑盒不可解释; CoreNLP由于训练样本的特殊性, 在固定短语上有预测能力, 实现了*相同表达方式, 不同主语的情况* 有一定的模型泛华效果
- CoreNLP提出的[RNTN](http://nlp.stanford.edu/~socherr/EMNLP2013_RNTN.pdf) 模型全部使用Java实现, 并且附有训练接口, 下文中会有更详细的解释, 且SmoothNLP提供了一些方便快捷的模型训练帮助脚本. 

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