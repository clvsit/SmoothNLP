package com.smoothnlp.nlp.util;
import com.hankcs.hanlp.corpus.occurrence.Occurrence;
import com.hankcs.hanlp.corpus.occurrence.PairFrequency;
import com.hankcs.hanlp.corpus.occurrence.TermFrequency;
import com.hankcs.hanlp.corpus.occurrence.TriaFrequency;
import com.smoothnlp.nlp.simple.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class PMIEntripyCalculator {

    public Occurrence occurrence;
    private boolean computed;
    private SegmentPipeline segment_pipe;
    private HashMap<String,Double> pmi_scores;
    private HashMap<String,Double> le_scores;
    private HashMap<String,Double> re_scores;
    private HashMap<String,Double> normalized_scores;  // normalized scores originally calculated in HanLP Package


    public PMIEntripyCalculator(){
        this.occurrence = new Occurrence();
        this.segment_pipe = new SegmentPipeline();

        this.pmi_scores = new HashMap<String,Double>();
        this.le_scores = new HashMap<String,Double>();
        this.re_scores = new HashMap<String,Double>();
        this.normalized_scores = new HashMap<String, Double>();
        this.computed = false;

    }

    public void addSentence(String inputText){
        inputText = inputText.replace(" ","");
        String[] tokenList = inputText.split("");
        this.occurrence.addAll(tokenList);
        this.computed = false;
    }

    public void addCorpus(String inputCorpus){
        addCorpus(inputCorpus,"\n");
    }

    public void addCorpus(String inputCorpus, String sep){
        String[] sentences = inputCorpus.split(sep);
        for (String sent: sentences){
            addSentence(sent);
        }
    }

    public void compute(){

        this.occurrence.compute();
        // 1-char token
//        Set<Map.Entry<String, TermFrequency>> unigrams = this.occurrence.getUniGram();
//        for (Map.Entry<String, TermFrequency> entry : unigrams){
//            TermFrequency value = entry.getValue();
//            String token = value.getTerm();
//        }

        // 2-char token
        Set<Map.Entry<String, PairFrequency>> bigrams = this.occurrence.getBiGram();
        for (Map.Entry<String, PairFrequency> entry : bigrams){
            PairFrequency value = entry.getValue();
            String token = value.first+value.second;
            this.pmi_scores.put(token,this.occurrence.computeMutualInformation(value));
            this.le_scores.put(token,this.occurrence.computeLeftEntropy(value));
            this.re_scores.put(token,this.occurrence.computeRightEntropy(value));
            this.normalized_scores.put(token,value.score);
        }

        // 3-char token
        Set<Map.Entry<String, TriaFrequency>> trigrams = this.occurrence.getTriGram();
        for (Map.Entry<String, TriaFrequency> entry : trigrams){
            TriaFrequency value = entry.getValue();
            if (value.delimiter==this.occurrence.RIGHT){   // only one-direction is considered for now
                String token = value.first+value.second + value.third;
                this.pmi_scores.put(token,this.occurrence.computeMutualInformation(value));
                this.le_scores.put(token,this.occurrence.computeLeftEntropy(value));
                this.re_scores.put(token,this.occurrence.computeRightEntropy(value));
                this.normalized_scores.put(token,value.score);
            }
        }
        this.computed = true;
    }

    public String[] getCoveredTokens(String inputText){
        ArrayList<String> coveredTokens = new ArrayList<String>();
        String[] segmentedTokens =this.segment_pipe.segmentText(inputText);
        for (String token: segmentedTokens){
            if (this.normalized_scores.containsKey(token)){
                coveredTokens.add(token);
            }
        }
        return coveredTokens.toArray(new String[coveredTokens.size()]);
    }

    public HashMap<String,HashMap<String,Double>> getTokenScores4Sentence(String inputText){
        String[] coveredTokens = getCoveredTokens(inputText);
        HashMap<String,HashMap<String,Double>> tokens2scores = new HashMap<String,HashMap<String,Double>>();
        for (String token:coveredTokens){
            HashMap<String,Double> tokenscore = new HashMap<String, Double>();
            tokenscore.put("pmi",this.pmi_scores.get(token));
            tokenscore.put("le",this.le_scores.get(token));
            tokenscore.put("re",this.re_scores.get(token));
            tokenscore.put("phrase_score",this.normalized_scores.get(token));
            tokens2scores.put(token,tokenscore);
        }
        return tokens2scores;
    }

    public String getTokenScores4SentenceJsonStr(String inputText){
        GsonBuilder gsonbuilder = new GsonBuilder();
        gsonbuilder.serializeSpecialFloatingPointValues();
        Gson gsonobject = gsonbuilder.create();
        HashMap<String,HashMap<String,Double>> tokens2scores= getTokenScores4Sentence(inputText);
        return gsonobject.toJson(tokens2scores);
    }

    public HashMap<String,Double> getPmi_scores(){
        return this.pmi_scores;
    }

    public HashMap<String,Double> getLe_scores(){
        return this.le_scores;
    }

    public HashMap<String,Double> getRe_scores(){
        return this.re_scores;
    }

    public HashMap<String,Double> getPhrase_scores(){
        return this.normalized_scores;
    }

    public static void main(String[] args){
        PMIEntripyCalculator pcalc = new PMIEntripyCalculator();
        pcalc.addSentence("五块钱虽然买不到多少");
        pcalc.compute();
        System.out.println(pcalc.getTokenScores4Sentence("五块钱虽然买不到多少").size());
        System.out.println(pcalc.getTokenScores4SentenceJsonStr("五块钱虽然买不到多少"));
    }

}
