from smoothnlp.jvm import LazyLoadingJClass
PMIEntropyLearner = LazyLoadingJClass("com.smoothnlp.nlp.learner.PMIEntropyLearner")
TextRankLeaner = LazyLoadingJClass("com.smoothnlp.nlp.learner.TextRankLeaner")
DocumentLearner = LazyLoadingJClass("com.smoothnlp.nlp.learner.DocumentLearner")
DocumentTokenLearner = LazyLoadingJClass("com.smoothnlp.nlp.learner.DocumentLearner$DocumentTokenLearner")
MoneyLearner = LazyLoadingJClass("com.smoothnlp.nlp.learner.money.MoneyLearner")