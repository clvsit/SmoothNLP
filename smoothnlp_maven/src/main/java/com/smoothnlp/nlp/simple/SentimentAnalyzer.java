package com.smoothnlp.nlp.simple;

import com.smoothnlp.nlp.SimplePipeline;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import sun.java2d.pipe.SpanShapeRenderer;
import edu.stanford.nlp.trees.Tree;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class SentimentAnalyzer implements SimplePipeline {

    public static InputStream CHINESE_DEFAULT_PROPS_INPUTSTREAM =  SentimentAnalyzer.class.getClass().getResourceAsStream("/StanfordCoreNLP-chinese.properties");
    public final Properties props;
    public final StanfordCoreNLP pipeline;

    public SentimentAnalyzer(){
        this.props = new Properties();
        try {
            props.load(CHINESE_DEFAULT_PROPS_INPUTSTREAM);
        } catch (IOException e) {
            e.printStackTrace();
        }
        props.setProperty("annotators", "tokenize,ssplit,pos,parse,lemma,ner,sentiment");
        this.pipeline = new StanfordCoreNLP(props);
    }

    public String getSentimentValue(String inputText){
        CoreDocument document = new CoreDocument(inputText);
        this.pipeline.annotate(document);
        CoreSentence sentence = document.sentences().get(0);
        return sentence.sentiment();
    }

    public String analyze(String inputText){
        return getSentimentValue(inputText);
    }

    public Tree getSentimentTree(String inputText){
        CoreDocument document = new CoreDocument(inputText);
        this.pipeline.annotate(document);
        CoreSentence sentence = document.sentences().get(0);
        System.out.println(sentence.coreMap().toShorterString());
        return sentence.sentimentTree();
    }

    public static void main(String[] args){
        SentimentAnalyzer sa = new SentimentAnalyzer();
        sa.getSentimentTree("今天天气不错");

    }
}
