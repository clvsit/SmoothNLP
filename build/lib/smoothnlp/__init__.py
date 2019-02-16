__name__ = "SmoothNLP"

global __nlp__
__nlp__=None

def initNLP(host="http://127.0.0.1",port=9000,lang="zh"):
    global __nlp__
    from stanfordcorenlp import StanfordCoreNLP
    __nlp__ = StanfordCoreNLP(host, port=port,lang=lang)

initNLP()

import smoothnlp.treebanks as treebanks
from smoothnlp.pipeline import sentence_split,tokenize,postag,number_recognize,money_recognize,ner_recognize



