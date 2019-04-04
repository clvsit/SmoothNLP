package com.smoothnlp.nlp.simple;

import com.smoothnlp.nlp.SmoothNLP;

import java.io.IOException;
import java.util.*;
import java.io.InputStream;
import java.io.FileInputStream;


import com.smoothnlp.nlp.simple.SimplePipeline;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.ling.CoreAnnotations;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class NormalizedNER implements SimplePipeline {

    public static InputStream CHINESE_DEFAULT_PROPS_INPUTSTREAM =  NormalizedNER.class.getClass().getResourceAsStream("/StanfordCoreNLP-chinese.properties");

    public Properties props;
    public StanfordCoreNLP pipeline;

    private boolean tokenizeBySpace;

    public NormalizedNER(){
        init();
        this.pipeline = new StanfordCoreNLP(props);
    }

    public NormalizedNER(boolean tokenizeBySpace){
        init();
        this.tokenizeBySpace = tokenizeBySpace;
        if (this.tokenizeBySpace=false){
            props.setProperty("tokenize.whitespace","true");
        }
        this.pipeline = new StanfordCoreNLP(props);
    }

    public NormalizedNER(boolean tokenizeBySpace, StanfordCoreNLP pipeline){
        this.tokenizeBySpace = tokenizeBySpace;
        this.pipeline = pipeline;
    }

    protected void init(){
        this.props = new Properties();
        try {
            props.load(CHINESE_DEFAULT_PROPS_INPUTSTREAM);
        } catch (IOException e) {
            e.printStackTrace();
        }
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
    }

    public ArrayList<HashMap<String,String>> getNormalizedNER(String inputText){
        if (this.tokenizeBySpace==true){
            inputText = SmoothNLP.segment_pipeline.segmentTextwithWhiteSpace(inputText);
        }
        CoreDocument document = new CoreDocument(inputText);
        this.pipeline.annotate(document);
//        document.sentences().get(0).dependencyParse().;
        ArrayList<HashMap<String,String>> nerRes = new ArrayList<HashMap<String,String>>();

        for (CoreEntityMention em : document.entityMentions()){
            HashMap<String,String> emMap = new HashMap<String,String>();
            emMap.put("text",em.text());
            emMap.put("tokens",em.tokens().toString());
//            emMap.put("charOffsets",em.charOffsets().toString());
            emMap.put("charStart",Integer.toString(em.charOffsets().first));
            emMap.put("charEnd",Integer.toString(em.charOffsets().second));
            emMap.put("entityType",em.entityType());
            if (em.coreMap().containsKey(CoreAnnotations.NormalizedNamedEntityTagAnnotation.class)){
                emMap.put("normalizedEntityTag",em.coreMap().get(CoreAnnotations.NormalizedNamedEntityTagAnnotation.class));
            }
            nerRes.add(emMap);
        }
        return nerRes;
    }

//    public String getNormalizedNERinStr(String inputText){
//        String[] arrayRes = this.getNormalizedNER(inputText);
//        Gson gsonobject = new Gson();
//        String jsonStr = gsonobject.toJson(arrayRes);
//        return jsonStr;
//    }

    public String analyze(String inputText){
        Gson gsonobject = new Gson();
        return gsonobject.toJson(getNormalizedNER(inputText));
    }

    public static void main(String[] args){
        NormalizedNER nner = new NormalizedNER(true);
        System.out.println(nner.analyze("我买了五斤苹果, 总共10元钱"));
    }

}
