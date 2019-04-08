package com.smoothnlp.nlp.learner;

public interface BaseLearner {
    public void fit(String inputText);
    public void fit(String[] inputCorpus);
    public String transform(String inputText);
    public String transform(String[] inputCorpus);
}
