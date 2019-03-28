package com.smoothnlp.nlp.learner;

public abstract class BaseLearner {
    public void fit(String[] sentences){};
    public void fit(String inputText){};
    public String transform(String inputText){ return ""; };

    public String[] transform(String[] sentences){
        String[] transformed_res = new String[sentences.length];
        for (int i=0;i<sentences.length;i++){
            transformed_res[i] = transform(sentences[0]);
        }
        return transformed_res;
    }
}
