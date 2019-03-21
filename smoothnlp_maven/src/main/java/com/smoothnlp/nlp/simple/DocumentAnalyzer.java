package com.smoothnlp.nlp.simple;

import com.smoothnlp.nlp.simple.SimplePipeline;
import com.smoothnlp.nlp.simple.NormalizedNER;
import com.smoothnlp.nlp.simple.SentimentAnalyzer;
import com.google.gson.Gson;
import sun.java2d.pipe.SpanShapeRenderer;

import java.util.HashMap;
import java.util.ArrayList;

public class DocumentAnalyzer implements SimplePipeline{

    private ArrayList<SimplePipeline> pipelines;

    public DocumentAnalyzer(){
        this.pipelines = new ArrayList<SimplePipeline>();
        this.pipelines.add(new NormalizedNER());
        this.pipelines.add(new SentimentAnalyzer());
    }

    public String analyze(String inputText){
        ArrayList<HashMap<String,String>> pipeline_results = new ArrayList<HashMap<String, String>>();


        for (SimplePipeline pipeline : this.pipelines){
            Gson gsonobject = new Gson();
            ArrayList<HashMap<String,String>> pipeline_output = gsonobject.fromJson(pipeline.analyze(inputText),pipeline_results.getClass());
            pipeline_results.addAll(pipeline_output);
        }
        Gson combined_gson = new Gson();
        return combined_gson.toJson(pipeline_results);
    }

    public static void main(String[] args){
        DocumentAnalyzer da = new DocumentAnalyzer();
        System.out.println(da.analyze("五元钱"));
    }
}
