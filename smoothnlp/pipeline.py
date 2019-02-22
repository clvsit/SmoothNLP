from smoothnlp import __nlp__
import json
import re

def sentence_split(text):
    regex = re.compile("[.。]|[!?！？]+")
    return [''.join(item) for item in zip(re.split(regex,text),re.findall(regex, text))]

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

def recognize_wrapper(text,tag:set={}):
    if isinstance(tag,str):
        tag = {tag}
    else:
        tag=set(tag)
    props = {'annotators': 'ner', 'pipelineLanguage': 'zh', 'outputFormat': 'json'}
    res = json.loads(__nlp__.annotate(text, properties=props))
    if len(res['sentences']) == 1:
        res = res['sentences'][0]
        if "entitymentions" in res:
            if not tag:
                return [{"normalized_entity": (en['normalizedNER']),
                     "type":en['ner'],
                     "start": en['characterOffsetBegin'],
                     "end": en['characterOffsetEnd']} for en in res['entitymentions']]
            return [{"normalized_entity": (en['normalizedNER']),
                     "type":en['ner'],
                     "start": en['characterOffsetBegin'],
                     "end": en['characterOffsetEnd']} for en in res['entitymentions'] if
                    en['ner'] in tag and 'normalizedNER' in en]

def ner_recognize(text):
    return recognize_wrapper(text)

def number_recognize(text):
    return recognize_wrapper(text,tag="NUMBER")

def money_recognize(text):
    return recognize_wrapper(text,tag="MONEY")

#def ner_recognize(text):
#    return recognize_wrapper(text,{"MONEY","NUMBER"})


if __name__=="__main__":
    print(sentence_split("你好,欢迎来到菲律宾。菲律宾的岛屿很漂亮.在菲律宾买房子也不贵"))
    print(tokenize("你好,欢迎来到菲律宾。"))
    print(postag("你好,欢迎来到菲律宾。菲律宾的岛屿很漂亮.在菲律宾买房子也不贵"))
    import re
    regex =re.compile("[.。]|[!?！？]+")
    text = "你好,欢迎来到菲律宾。菲律宾的岛屿很漂亮.在菲律宾买房子也不贵"
    print(re.split(regex,text))
    print(ner_recognize("两百五十"))
