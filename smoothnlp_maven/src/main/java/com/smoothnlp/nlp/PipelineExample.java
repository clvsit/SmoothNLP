package com.smoothnlp.nlp;

import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.*;

import java.util.*;

import java.io.IOException;
import java.util.*;
import java.io.InputStream;
import edu.stanford.nlp.ling.CoreAnnotation;



import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;


public class PipelineExample {

    public static String text = "两千五百元";

    public static void main(String[] args){
        InputStream input = PipelineExample.class.getClass().getResourceAsStream("/chinese.properties");
        Properties props = new Properties();
        try {
            props.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        props.setProperty("annotators", "tokenize,ssplit,pos,parse,lemma,ner,sentiment");


        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create a document object
        CoreDocument document = new CoreDocument(text);

        // annnotate the document
        pipeline.annotate(document);

        CoreSentence sentence = document.sentences().get(0);
        System.out.println(sentence.text());

        List<String> nerTags = sentence.nerTags();
        System.out.println("Example: ner tags");
        System.out.println(nerTags);
        System.out.println();

        List<CoreEntityMention> entityMentions = sentence.entityMentions();
        System.out.println("Example: entity mentions");
        System.out.println(entityMentions);
        System.out.println();

        Annotation doc = new Annotation(text);
        pipeline.annotate(doc);
        for (CoreLabel token: doc.get(CoreAnnotations.TokensAnnotation.class)){
            System.out.println(token);
        }

        for (CoreMap cm : doc.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree tree = cm.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
            System.out.println("sentiment score: ");
            System.out.println(sentiment);
//            String partText = sentence.toString();
//            if (partText.length() > longest) {
//                mainSentiment = sentiment;
//                longest = partText.length();
//            }

        }

        System.out.println(doc.get(CoreAnnotations.NormalizedNamedEntityTagAnnotation.class));
        System.out.println(doc.get(CoreAnnotations.NamedEntityTagAnnotation.class));

        for (CoreEntityMention em : document.entityMentions()){
            System.out.println(em.coreMap().toShorterString());
        }

//        System.out.println(sentence.sentimentTree().nodeString());
        System.out.println(sentence.sentimentTree().toString());
        System.out.println(sentence.sentiment());


    }

}
