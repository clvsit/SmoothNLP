import smoothnlp
from smoothnlp.jvm import LazyLoadingJClass,SafeJClass
ner = SafeJClass("com.smoothnlp.nlp.simple.NormalizedNER")
sentiment = SafeJClass("com.smoothnlp.nlp.simple.SentimentAnalyzer")