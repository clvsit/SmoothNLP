package com.smoothnlp.nlp.HiveUDF;
import com.smoothnlp.nlp.simple.NormalizedNER;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public class NormalizedNERudf extends UDF{

    public static NormalizedNER normalizedNER = new NormalizedNER();

    public Text evaluate(Text input){
        String inputText = input.toString();
        String outputString = normalizedNER.getNormalizedNERinStr(inputText);
        Text textres = new Text();
        textres.set(outputString);
        return textres;
    }

    public static void main(String[] args){
//        NormalizedNER nner = new NormalizedNER();
        System.out.println(normalizedNER.getNormalizedNERinStr("地址是浙江省台州市路桥区金清镇人民南路86号 收件人陈巧 电话是13224053520"));
        NormalizedNERudf nnerUDF = new NormalizedNERudf();
        Text sampleText = new Text();
        sampleText.set("地址是浙江省台州市路桥区金清镇人民南路86号 收件人陈巧 电话是13224053520");
        Text sampleOutput = nnerUDF.evaluate(sampleText);
        System.out.println(sampleOutput.toString());
    }
}
