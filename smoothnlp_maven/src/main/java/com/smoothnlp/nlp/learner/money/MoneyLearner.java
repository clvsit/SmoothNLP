package com.smoothnlp.nlp.learner.money;
import com.smoothnlp.nlp.learner.BaseLearner;
import com.smoothnlp.nlp.simple.NormalizedNER;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.HashMap;
import java.util.ArrayList;

public class MoneyLearner extends BaseLearner {

    public static InputStream CHINESE_DEFAULT_PROPS_INPUTSTREAM =  NormalizedNER.class.getClass().getResourceAsStream("/StanfordCoreNLP-chinese.properties");

    public Properties props;
    public StanfordCoreNLP pipeline;

    protected NormalizedNER normalizedNER;

    private boolean tokenizeBySpace;

    public MoneyLearner(){
        super();
        init();
        this.pipeline = new StanfordCoreNLP(props);
        this.tokenizeBySpace = true;
        this.normalizedNER = new NormalizedNER(this.tokenizeBySpace,this.pipeline);
    }

    public MoneyLearner(boolean tokenizeBySpace){
        super();
        init();
        this.tokenizeBySpace = tokenizeBySpace;
        if (this.tokenizeBySpace=false){
            props.setProperty("tokenize.whitespace","true");
        }
        this.pipeline = new StanfordCoreNLP(props);
        this.normalizedNER = new NormalizedNER(this.tokenizeBySpace,this.pipeline);
    }

    protected void init(){
        this.props = new Properties();
        try {
            props.load(CHINESE_DEFAULT_PROPS_INPUTSTREAM);
        } catch (IOException e) {
            e.printStackTrace();
        }
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse");
    }

    public ArrayList<HashMap<String,String>> getMoneyNER(String inputText){
        ArrayList<HashMap<String,String>> nerResList = this.normalizedNER.getNormalizedNER(inputText);
        ArrayList<HashMap<String,String>> moneyResList = new ArrayList<HashMap<String,String>>();
        for (HashMap<String,String> nerRes : nerResList){
            if (nerRes.get("entityType")=="MONEY"){
                moneyResList.add(nerRes);
            }
        }
        return moneyResList;
    }



    @Override
    public String transform(String inputText){
        ArrayList<HashMap<String,String>> moneyResList = this.getMoneyNER(inputText);
        Gson gsonobject = new Gson();
        System.out.println(gsonobject.toJson(moneyResList));
        CoreDocument document = new CoreDocument(inputText);
        this.pipeline.annotate(document);
        System.out.println(document.sentences().get(0));
        SemanticGraph semanticGraph = document.sentences().get(0).dependencyParse();
        System.out.println(semanticGraph.getNodeByIndexSafe(1));
        return "";
    }

    public static void main(String[] args){
        MoneyLearner ml = new MoneyLearner();
        ml.transform("我买了五斤苹果, 总共10元钱");
    }

}
