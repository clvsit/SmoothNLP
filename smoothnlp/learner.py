from smoothnlp.jvm import LazyLoadingJClass
PMIEntropyLearner = LazyLoadingJClass("com.smoothnlp.nlp.leaner.PMIEntropyLearner")
TextRankLeaner = LazyLoadingJClass("com.smoothnlp.nlp.leaner.TextRankLeaner")

_indexers = ["token","tokenIndex","charStart","charEnd"]

