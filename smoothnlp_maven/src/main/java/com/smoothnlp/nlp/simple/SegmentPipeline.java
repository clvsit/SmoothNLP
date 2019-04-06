package com.smoothnlp.nlp.simple;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.io.ByteArrayStream;
import java.io.FileInputStream;
import com.hankcs.hanlp.seg.common.Term;


import java.io.IOException;
import java.util.*;
import com.hankcs.hanlp.corpus.occurrence.Occurrence;
import com.smoothnlp.nlp.simple.SimplePipeline;
import com.smoothnlp.nlp.SmoothNLP;
import com.google.gson.Gson;

import com.hankcs.hanlp.model.crf.CRFSegmenter;
import org.apache.commons.lang3.StringUtils;

public class SegmentPipeline implements SimplePipeline{

    private String mode;

    public SegmentPipeline(){
        this.mode = SmoothNLP.SEGMENT_MODE;
    }

    public SegmentPipeline(String mode){
        this.mode = mode;
    }

    public String[] segmentText(String inputText){
        if (this.mode =="Hanlp.default"){
            List<Term> termList = HanLP.segment(inputText);
            String[] strList = new String[termList.size()];
            int i = 0;
            for (Term word : termList)
            {
                strList[i] = word.word;
                ++i;
            }
            return strList;
        }
        // this should never be hit
        return new String[1];
    }

    public String segmentTextwithWhiteSpace(String inputText){
        return StringUtils.join(this.segmentText(inputText)," ");
    }

    public ArrayList<HashMap<String,String>> analyzeRaw(String textInput){
        int start = 0;
        int tokenIndex = 0;
        String[] segmented_res = segmentText(textInput);
        ArrayList<HashMap<String,String>> res_list = new ArrayList<HashMap<String,String>>();
        for (String word:segmented_res){
            int end = start + word.length();
            HashMap<String,String> word_res = new HashMap<String,String>();
            word_res.put("charStart",String.valueOf(start));
            word_res.put("charEnd",String.valueOf(end));
            word_res.put("tokenIndex", Integer.toString(tokenIndex));
            tokenIndex+=1;
            word_res.put("token",word);
            start += word.length();
            res_list.add(word_res);
        }
        return res_list;
    }

    public String analyze(String textInput){
        ArrayList<HashMap<String,String>> res_list = this.analyzeRaw(textInput);
        Gson gsonobject = new Gson();
        return gsonobject.toJson(res_list);
    }

    public static void main(String[] args) throws IOException {
        SegmentPipeline sp = new SegmentPipeline();
        System.out.println(sp.segmentTextwithWhiteSpace("我买了五斤苹果, 总共10元钱"));
        System.out.println(sp.analyze("我买了五斤苹果, 总共10元钱"));
//        String modelPath = "pku199801/cws.txt.bin";
//        CRFSegmenter s = new CRFSegmenter(modelPath);
//        List<String> wordList = s.segment("商品和服务");
//        System.out.println(wordList);
    }

}
