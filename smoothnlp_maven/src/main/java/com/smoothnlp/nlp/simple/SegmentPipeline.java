package com.smoothnlp.nlp.simple;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import java.util.*;
import com.hankcs.hanlp.corpus.occurrence.Occurrence;
import com.smoothnlp.nlp.simple.SimplePipeline;
import com.google.gson.Gson;

public class SegmentPipeline implements SimplePipeline{

    private String mode;

    public SegmentPipeline(){
        this.mode = "Hanlp";
    }

    public SegmentPipeline(String mode){
        this.mode = mode;
    }

    public String[] segmentText(String inputText){
        if (this.mode =="Hanlp"){
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

    public String analyze(String textInput){
        int start = 0;
        String[] segmented_res = segmentText(textInput);
        ArrayList<HashMap<String,String>> res_list = new ArrayList<HashMap<String,String>>();
        for (String word:segmented_res){
            int end = start + word.length();
            HashMap<String,String> word_res = new HashMap<String,String>();
            word_res.put("charStart",String.valueOf(start));
            word_res.put("chatEnd",String.valueOf(end));
            word_res.put("token",word);
            start += word.length();
            res_list.add(word_res);
        }
        Gson gsonobject = new Gson();
        return gsonobject.toJson(res_list);
    }

    public static void main(String[] args){
        SegmentPipeline sp = new SegmentPipeline();
        System.out.println(sp.analyze("五块钱虽然买不到多少, 但是五块钱还是很不错的"));
    }


}
