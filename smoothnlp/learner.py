from smoothnlp.jvm import LazyLoadingJClass
PMIEntropyLearner = LazyLoadingJClass("com.smoothnlp.nlp.learner.PMIEntropyLearner")
TextRankLeaner = LazyLoadingJClass("com.smoothnlp.nlp.learner.TextRankLeaner")

_indexers = ["token","tokenIndex","charStart","charEnd"]

