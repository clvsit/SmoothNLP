package com.smoothnlp.nlp.learner;

import com.google.gson.Gson;
import com.smoothnlp.nlp.SmoothNLP;
import com.smoothnlp.nlp.learner.BaseLearner;

import java.util.ArrayList;
import java.util.HashMap;

public class DocumentLearner extends BaseLearner{

    private BaseLearner[] subLearners;

    public DocumentLearner(){
        super();
        this.subLearners = new BaseLearner[]{
                new PMIEntropyLearner(),
                new TextRankLeaner()
        };
    }

    @Override
    public void fit(String inputText){
        for (BaseLearner l : this.subLearners){
            l.fit(inputText);
        }
        this.fitted = false;
    }

    @Override
    protected void compute(){
        for (BaseLearner l : this.subLearners){
            l.compute();
        }
    }

    @Override
    public HashMap<String,String> encodeToken(String token){
        HashMap<String,String> tokenRes = new HashMap<String,String>();
        for (BaseLearner l : this.subLearners){
            tokenRes.putAll(l.encodeToken(token));
        }
        return tokenRes;
    }

    public static void main(String[] args){
        String sampletext1 = "今天天气不错,天气这么好, 真应该出去玩耍呀";
        String sampletext2 = "今天天气不错, 但是如果不出去玩, 在家里写代码也会很开心";
        BaseLearner pleaner = new DocumentLearner();
        pleaner.fit(sampletext1);
        pleaner.fit(sampletext2);
        System.out.println(pleaner.transform(sampletext1));
    }

}
