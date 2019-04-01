package com.smoothnlp.nlp.learner;
import com.google.gson.Gson;
import com.smoothnlp.nlp.SmoothNLP;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class BaseLearner {

    protected boolean fitted;

    public BaseLearner(){
        this.fitted = false;
    }

    public HashMap<String,String> encodeToken(String token){
        return new HashMap<String,String>();
    }

    public void fit(String inputText){};
    public void fit(String[] sentences){
        for (String sent: sentences){ this.fit(sent);}
    };

    public void fit(String inputCorpus, String sep){
        String[] sentences = inputCorpus.split(sep);
        this.fit(sentences);
    }

    protected void compute(){};

    public String transform(String inputText){
        if (!this.fitted){
            this.compute();
            this.fitted = true;
        }
        String[] inputTokens = SmoothNLP.segment_pipeline.segmentText(inputText);
        int charIndex = 0;
        ArrayList<HashMap<String,String>> resList = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i< inputTokens.length; i++){
            HashMap<String,String> tokenRes = this.encodeToken(inputTokens[i]);
            tokenRes.put("token",inputTokens[i]);
            tokenRes.put("tokenIndex",Integer.toString(i));
            tokenRes.put("charStart",Integer.toString(charIndex));
            charIndex+=inputTokens[i].length();
            tokenRes.put("charEnd",Integer.toString(charIndex));
            resList.add(tokenRes);
        }
        Gson gsonobject = new Gson();
        return gsonobject.toJson(resList);
    }

    public String[] transform(String[] sentences){
        String[] transformed_res = new String[sentences.length];
        for (int i=0;i<sentences.length;i++){
            transformed_res[i] = transform(sentences[0]);
        }
        return transformed_res;
    }
}
