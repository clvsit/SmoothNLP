package com.smoothnlp.nlp.simple;
import com.google.gson.Gson;
import com.hankcs.hanlp.seg.common.Term;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.hankcs.hanlp.HanLP;

import com.smoothnlp.nlp.SmoothNLP;

public class PosTagAnalyzer implements SimplePipeline {

    private String mode;

    public PosTagAnalyzer(){
        this.mode = SmoothNLP.POSTAG_MODE;
    }

    public PosTagAnalyzer(String mode){
        this.mode = mode;
    }

    public String[] PosTagText(String inputText){
        if (this.mode =="Hanlp.default"){
            List<Term> termList = HanLP.segment(inputText);
            String[] strList = new String[termList.size()];
            int i = 0;
            for (Term word : termList)
            {
                strList[i] = word.nature.toString();
                ++i;
            }
            return strList;
        }
        // this should never be hit
        return new String[1];
    }

    public String analyze(String textInput){
        int start = 0;
        String[] postag_res = PosTagText(textInput);
        ArrayList<HashMap<String,String>> res_list = new ArrayList<HashMap<String,String>>();
        int tokenIndex = 0;
        for (String tag:postag_res){
            HashMap<String,String> word_res = new HashMap<String,String>();
            word_res.put("tokenIndex", Integer.toString(tokenIndex));
            tokenIndex+=1;
            word_res.put("postag",tag);
            res_list.add(word_res);
        }
        Gson gsonobject = new Gson();
        return gsonobject.toJson(res_list);
    }


}
