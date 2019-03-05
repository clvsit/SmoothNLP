package com.smoothnlp.nlp.simple;

import java.io.IOException;
import java.util.*;
import java.io.InputStream;
import java.io.FileInputStream;


import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.ling.CoreAnnotations;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class NormalizedNER {

    private static String sampleText = "地址是浙江省台州市路桥区金清镇人民南路86号 收件人陈巧 电话是13224053520 ";
    public static InputStream CHINESE_DEFAULT_PROPS_INPUTSTREAM =  NormalizedNER.class.getClass().getResourceAsStream("/StanfordCoreNLP-chinese.properties");

//    public static InputStream CHINESE_DEFAULT_PROPS_INPUTSTREAM = New FileInputStr/
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

    public String[] getNormalizedNER(String inputText){
        CoreDocument document = new CoreDocument(inputText);
        this.pipeline.annotate(document);
        ArrayList<String> nerRes = new ArrayList<String>();
        for (CoreEntityMention em : document.entityMentions()){
            HashMap<String,String> emMap = new HashMap<String,String>();
            emMap.put("text",em.text());
            emMap.put("tokens",em.tokens().toString());
            emMap.put("charOffsets",em.charOffsets().toString());
            emMap.put("entityType",em.entityType());

//            CoreMap coreMap = em.coreMap();
//            for (Class k: coreMap.keySet()){
//                System.out.println(coreMap.get(k));
//                System.out.println(k);
//            }
            Gson gsonobject = new Gson();
            String jsonStr = gsonobject.toJson(emMap);
            nerRes.add(jsonStr);
        }
        return nerRes.toArray(new String[nerRes.size()]);
    }

    public String getNormalizedNERinStr(String inputText){
        String[] arrayRes = this.getNormalizedNER(inputText);
        Gson gsonobject = new Gson();
        String jsonStr = gsonobject.toJson(arrayRes);
        return jsonStr;
    }

    public static void main(String[] args){
        NormalizedNER nner = new NormalizedNER();
        String res = Arrays.toString(nner.getNormalizedNER("五块钱"));
//        nner.getNormalizedNER("江西省吉安市青原区河东街道科教路井大阳光城三期一单元29栋907室罗勇军13530943730");
        System.out.println(res);
    }
}
