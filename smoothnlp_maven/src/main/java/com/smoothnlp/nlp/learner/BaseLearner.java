package com.smoothnlp.nlp.learner;


public abstract class BaseLearner {

    protected boolean fitted;

    public BaseLearner(){
        this.fitted = false;
    }

    public void fit(String inputText){};
    public void fit(String[] sentences){
        for (String sent: sentences){ this.fit(sent);}
    };

    public void fit(String inputCorpus, String sep){
        String[] sentences = inputCorpus.split(sep);
        this.fit(sentences);
    }

    public String transform(String inputText){ return ""; };

    public String[] transform(String[] sentences){
        String[] transformed_res = new String[sentences.length];
        for (int i=0;i<sentences.length;i++){
            transformed_res[i] = transform(sentences[0]);
        }
        return transformed_res;
    }
}
