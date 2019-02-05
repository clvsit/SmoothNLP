__name__ = "SmoothNLP"

import smoothnlp.treebanks as treebanks
global __nlp__
from stanfordcorenlp import StanfordCoreNLP

def initNLP(host="127.0.0.1",port=9000,lang="zh"):
    global __nlp__
    __nlp__ = StanfordCoreNLP(host, port=port,lang=lang)