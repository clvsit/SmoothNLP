# Sentiment Study -- SmoothNLP


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