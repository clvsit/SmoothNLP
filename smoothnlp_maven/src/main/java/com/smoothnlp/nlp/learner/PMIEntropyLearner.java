package com.smoothnlp.nlp.learner;


import com.smoothnlp.nlp.util.PMIEntripyCalculator;

import java.util.HashMap;

public class PMIEntropyLearner extends BaseLearner {

    private PMIEntripyCalculator pmicalc;
    private boolean computed;

    public PMIEntropyLearner(){
        this.pmicalc = new PMIEntripyCalculator();
        this.computed = false
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

        return "";
    }

}
