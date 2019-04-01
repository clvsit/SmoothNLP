package com.smoothnlp.nlp.learner;

import com.smoothnlp.nlp.SmoothNLP;
import com.smoothnlp.nlp.util.PMIEntripyCalculator;

import java.util.HashMap;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PMIEntropyLearner extends BaseLearner {

    private PMIEntripyCalculator pmicalc;

    // 2 be developed
//    private HashMap<String,Double> pmi_scores;
//    private HashMap<String,Double> le_scores;
//    private HashMap<String,Double> re_scores;
//    private HashMap<String,Double> normalized_scores;  // normalized scores originally calculated in HanLP Package


    public PMIEntropyLearner(){
        super();
        this.pmicalc = new PMIEntripyCalculator();
        this.fitted = false;
    }

    @Override
    public void fit(String inputText){
        this.pmicalc.addSentence(inputText);
        this.fitted = false;
    }

    @Override
    protected void compute(){
        this.pmicalc.compute();
        this.fitted = true;
    }

    @Override
    public HashMap<String,String> encodeToken(String token){
        HashMap<String,String> tokenRes = new HashMap<String, String>();
        HashMap<String,Double> token2scores = this.pmicalc.getTokenScores(token);
        for (String t: token2scores.keySet()){
            tokenRes.put(t,Double.toString(token2scores.get(t)));
        }
        return tokenRes;
    }


    public static void main(String[] args){
        String sampletext1 = "五块钱虽然买不到多少";
        String sampletext2 = "五块钱也算是钱";
        PMIEntropyLearner pleaner = new PMIEntropyLearner();
        pleaner.fit(sampletext1);
        pleaner.fit(sampletext2);
        System.out.println(pleaner.transform(sampletext1));
    }

}
