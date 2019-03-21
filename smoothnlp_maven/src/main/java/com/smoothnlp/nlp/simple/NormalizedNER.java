package com.smoothnlp.nlp.simple;

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

    public final Properties props;
    public final StanfordCoreNLP pipeline;

    public NormalizedNER(){
        this.props = new Properties();
        try {
            props.load(CHINESE_DEFAULT_PROPS_INPUTSTREAM);
        } catch (IOException e) {
            e.printStackTrace();
        }
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
        this.pipeline = new StanfordCoreNLP(props);
    }

    public String getNormalizedNER(String inputText){
        CoreDocument document = new CoreDocument(inputText);
        this.pipeline.annotate(document);
        ArrayList<HashMap<String,String>> nerRes = new ArrayList<HashMap<String,String>>();
        Gson gsonobject = new Gson();
        for (CoreEntityMention em : document.entityMentions()){
            HashMap<String,String> emMap = new HashMap<String,String>();
            emMap.put("text",em.text());
            emMap.put("tokens",em.tokens().toString());
            emMap.put("charOffsets",em.charOffsets().toString());
            emMap.put("entityType",em.entityType());
            if (em.coreMap().containsKey(CoreAnnotations.NormalizedNamedEntityTagAnnotation.class)){
                emMap.put("normalizedEntityTag",em.coreMap().get(CoreAnnotations.NormalizedNamedEntityTagAnnotation.class));
            }
            nerRes.add(emMap);
        }
        String jsonStr = gsonobject.toJson(nerRes);
        return jsonStr;
    }

//    public String getNormalizedNERinStr(String inputText){
//        String[] arrayRes = this.getNormalizedNER(inputText);
//        Gson gsonobject = new Gson();
//        String jsonStr = gsonobject.toJson(arrayRes);
//        return jsonStr;
//    }

    public String analyze(String inputText){
        return getNormalizedNER(inputText);
    }

}
