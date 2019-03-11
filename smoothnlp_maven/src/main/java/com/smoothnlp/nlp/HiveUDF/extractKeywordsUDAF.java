package com.smoothnlp.nlp.HiveUDF;

import org.apache.hadoop.hive.ql.exec.UDAF;
import org.apache.hadoop.hive.ql.exec.UDAFEvaluator;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import java.util.ArrayList;
import com.hankcs.hanlp.HanLP;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import java.lang.NullPointerException;

public class extractKeywordsUDAF extends UDAF{

    public static class extractKeywordsUDAFEvaluator implements UDAFEvaluator{

        private Text allText;

        public extractKeywordsUDAFEvaluator()
        {
            super();
            init();
        }

        public void init(){
            allText = new Text();
        }

        public boolean iterate(Text inputText){
            if (inputText == null){
                return true;
            }
            String allString = allText.toString() +"\n" + inputText.toString();
            allText.set(allString);
            return true;
        }

        public Text terminatePartial()
        {
            return terminate();
        }

        public Text terminate(){
            Text res = new Text();
            List<String> keywords = HanLP.extractKeyword(allText.toString(),10);
            res.set(StringUtils.join(keywords,";"));
            return res;
        }

        public boolean merge(Text other){
            return iterate(other);
        }

    }

}
