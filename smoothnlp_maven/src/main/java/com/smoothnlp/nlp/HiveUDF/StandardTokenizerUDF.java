package com.smoothnlp.nlp.HiveUDF;

import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import java.util.List;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;

public class StandardTokenizerUDF extends UDF{

    public Text evaluate(Text inputStr) {
        String text = inputStr.toString();
        String res = tokenize(text);
        Text textres = new Text();
        textres.set(res);
        return textres;
    }

    public static String tokenize(String text){
        text = text.replaceAll("\\s","");
        StandardTokenizer s = new StandardTokenizer();
        List<Term> terms = s.segment(text);
        ArrayList<String> res = new ArrayList<String>();
        for (Term t:terms){
            res.add(t.word);
        }
        return StringUtils.join(res," ");
    }


    public static void main(String[] args){
        String inputStr = "商品便宜又好用";
        System.out.println(tokenize(inputStr));
    }

}
