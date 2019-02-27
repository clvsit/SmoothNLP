## NER Recognizer 实体识别模型
命名实体识别在大多数框架中都使用了基于CRF-概率随机场的模型, 
在[CoreNLP](https://stanfordnlp.github.io/CoreNLP/ner.html)的实现中, 使用了自实现的CRF, 
[Hanlp](https://github.com/hankcs/HanLP)中支持了扩展CRF++与自实现的
[Perceptron感知机](https://github.com/hankcs/HanLP/wiki/%E7%BB%93%E6%9E%84%E5%8C%96%E6%84%9F%E7%9F%A5%E6%9C%BA%E6%A0%87%E6%B3%A8%E6%A1%86%E6%9E%B6)的模型, 
本项目将介绍两者模型的训练方式, Demo代码及效果对比.
特别感谢[ChineseNER](https://github.com/buppt/ChineseNER) 收集的来自Boson, MSRA 和人民日报的标注数据 

### CoreNLP框架
#### Traing Data Structure 训练集数据格式
```text
浙	B_product_name
江	M_product_name
在	M_product_name
线	M_product_name
杭	M_product_name
州	E_product_name
4	B_time
月	M_time
2	M_time
5	M_time
日	E_time
讯	O
```

#### Model Training 模型训练
```angular2
java -Xmx2g -cp corenlp-chinese-smoothnlp-*-jar-with-dependencies.jar edu.stanford.nlp.ie.crf.CRFClassifier -prop ner.model.props
```
关于模型配置文件更详细的信息, 可以看[官方FAQ文档](https://nlp.stanford.edu/software/crf-faq.html#a)
和[具体参数说明](https://nlp.stanford.edu/nlp/javadoc/javanlp/edu/stanford/nlp/ie/NERFeatureFactory.html)

*模型输出* : model.ser.gz





