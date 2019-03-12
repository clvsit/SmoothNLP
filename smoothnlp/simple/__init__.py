import smoothnlp
from smoothnlp.jvm import LazyLoadingJClass,SafeJClass
ner = LazyLoadingJClass("com.smoothnlp.nlp.simple.NormalizedNER")
sentiment = LazyLoadingJClass("com.smoopythnlp.nlp.simple.SentimentAnalyzer")