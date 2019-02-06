from smoothnlp import __nlp__
import json
import re

def sentence_split(text):
    regex = re.compile("[.。]|[!?！？]+")
    return [''.join(item) for item in zip(re.split(regex,text),re.findall(regex, text))]
    # props ={'annotators': 'ssplit', 'pipelineLanguage': 'zh', 'outputFormat': 'json'}
    # res = json.loads(__nlp__.annotate(text, properties=props))
    # sents = [''.join([t['word'] for t in r['tokens']]) for r in res['sentences']]
    # return sents

def tokenize(text):
    props = {'annotators': 'tokenize', 'pipelineLanguage': 'zh', 'outputFormat': 'json'}
    res = json.loads(__nlp__.annotate(text, properties=props))
    tokens = ' '.join([t['word'] for t in res['tokens']])
    return tokens

def postag(text):
    props = {'annotators': 'pos', 'pipelineLanguage': 'zh', 'outputFormat': 'json'}
    res = json.loads(__nlp__.annotate(text, properties=props))
    if len(res['sentences'])==1:
        res = res['sentences'][0]
        tokens_postaggers = ' '.join([t['word']+"/"+t['pos'] for t in res['tokens']])
        return tokens_postaggers
    else:
        return " ".join([postag(sent) for sent in sentence_split(text)])

if __name__=="__main__":
    print(sentence_split("你好,欢迎来到菲律宾。菲律宾的岛屿很漂亮.在菲律宾买房子也不贵"))
    print(tokenize("你好,欢迎来到菲律宾。"))
    print(postag("你好,欢迎来到菲律宾。菲律宾的岛屿很漂亮.在菲律宾买房子也不贵"))
    import re
    regex =re.compile("[.。]|[!?！？]+")
    text = "你好,欢迎来到菲律宾。菲律宾的岛屿很漂亮.在菲律宾买房子也不贵"
    print(re.split(regex,text))
    print()
