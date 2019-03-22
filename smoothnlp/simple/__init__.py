import smoothnlp
from smoothnlp.jvm import LazyLoadingJClass,SafeJClass
ner = LazyLoadingJClass("com.smoothnlp.nlp.simple.NormalizedNER")
sentiment = LazyLoadingJClass("com.smoothnlp.nlp.simple.SentimentAnalyzer")
DocumentAnalyzer = LazyLoadingJClass("com.smoothnlp.nlp.simple.DocumentAnalyzer")
from smoothnlp import sentence_split as ssplit
import json

class CorpurAnalyzer():
    def __init__(self,document_analyzer:DocumentAnalyzer=None):
        if document_analyzer is None:
            self.document_analyzer = DocumentAnalyzer()
        else:
            self.document_analyzer = document_analyzer

    def analyze(self,inputCorpus:str):
        sents = ssplit(inputCorpus)
        sents_analyze_res = [json.loads(self.document_analyzer.analyze(sent)) for sent in sents]
        for i in range(len(sents_analyze_res)):
            for j in range(len(sents_analyze_res[i])):
                sents_analyze_res[i][j]['sindex'] = i
        sents_analyze_res = [res for sent_res in sents_analyze_res for res in sent_res]
        return json.dumps(sents_analyze_res,ensure_ascii=False)


