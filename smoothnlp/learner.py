from smoothnlp.jvm import LazyLoadingJClass
PMIEntropyLearner = LazyLoadingJClass("com.smoothnlp.nlp.learner.PMIEntropyLearner")
TextRankLeaner = LazyLoadingJClass("com.smoothnlp.nlp.learner.TextRankLeaner")
DocumentLearner = LazyLoadingJClass("com.smoothnlp.nlp.learner.DocumentLearner")
MoneyLearner = LazyLoadingJClass("com.smoothnlp.nlp.learner.money.MoneyLearner")


_indexers = ["token","tokenIndex","charStart","charEnd"]

