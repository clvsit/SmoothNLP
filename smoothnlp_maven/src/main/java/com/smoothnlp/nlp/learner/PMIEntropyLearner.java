package com.smoothnlp.nlp.learner;

import com.smoothnlp.nlp.SmoothNLP;
import com.smoothnlp.nlp.util.PMIEntripyCalculator;

import java.util.HashMap;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PMIEntropyLearner extends BaseLearner {

    private PMIEntripyCalculator pmicalc;
    private boolean computed;

    public PMIEntropyLearner(){
        this.pmicalc = new PMIEntripyCalculator();
        this.computed = false;
    }

    @Override
    public void fit(String inputText){
        this.pmicalc.addSentence(inputText);
        this.fitted = true;
        this.computed = false;
    }

    protected void computePMIE(){
        this.pmicalc.compute();
        this.computed = true;
    }

    @Override
    public String transform(String inputText){
        if (!this.computed){
            computePMIE();
        }
        HashMap<String, HashMap<String,Double>> tokens2scores = this.pmicalc.getTokenScores4Sentence(inputText);
        String[] inputTokens = SmoothNLP.segment_pipeline.segmentText(inputText);
        int charIndex = 0;
        ArrayList<HashMap<String,String>> resList = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i< inputTokens.length; i++){
            HashMap<String,String> tokenRes = new HashMap<String, String>();
            tokenRes.put("token",inputTokens[i]);
            tokenRes.put("tokenIndex",Integer.toString(i));
            tokenRes.put("charStart",Integer.toString(charIndex));
            charIndex+=inputTokens[i].length();
            tokenRes.put("charEnd",Integer.toString(charIndex));
            if (tokens2scores.containsKey(inputTokens[i])){
                HashMap<String,Double> token2score = tokens2scores.get(inputTokens[i]);
                for (String score_name: token2score.keySet()){
                    tokenRes.put(score_name,Double.toString(token2score.get(score_name)));
                }
            }
            resList.add(tokenRes);
        }
        Gson gsonobject = new Gson();
        return gsonobject.toJson(resList);
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
