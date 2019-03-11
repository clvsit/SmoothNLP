package com.smoothnlp.nlp.HiveUDF;
import com.smoothnlp.nlp.simple.NormalizedNER;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public class NormalizedNERudf extends UDF{

    public static NormalizedNER normalizedNER;

    public NormalizedNERudf(){
        super();
        try{
            normalizedNER = new NormalizedNER();
        }catch(NullPointerException e){
            System.out.println(e.getMessage());
        }

    }

    public Text evaluate(Text input){
        String inputText = input.toString();
        String outputString = normalizedNER.getNormalizedNERinStr(inputText);
        Text textres = new Text();
        textres.set(outputString);
        return textres;
    }

    public static void main(String[] args){
//        NormalizedNER nner = new NormalizedNER();
        NormalizedNERudf nnerUDF = new NormalizedNERudf();
        Text sampleText = new Text();
        sampleText.set("地址是浙江省台州市路桥区金清镇人民南路999号");
        Text sampleOutput = nnerUDF.evaluate(sampleText);
        System.out.println(sampleOutput.toString());
    }
}
