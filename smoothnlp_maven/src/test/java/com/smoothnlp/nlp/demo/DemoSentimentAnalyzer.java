package com.smoothnlp.nlp.demo;

import com.smoothnlp.nlp.simple.SentimentAnalyzer;

public class DemoSentimentAnalyzer {
    public static void main(String[] args){
        String sampleText = "今天天气不错";
        SentimentAnalyzer ssa = new SentimentAnalyzer();
        System.out.print("原文: "+sampleText+"; 情感判别: ");
        System.out.println(ssa.getSentimentValue(sampleText));
        System.out.println(ssa.getSentimentTree(sampleText).toString());
    }
}
