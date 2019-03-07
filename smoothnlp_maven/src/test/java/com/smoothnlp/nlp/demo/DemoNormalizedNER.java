package com.smoothnlp.nlp.demo;

import com.smoothnlp.nlp.simple.NormalizedNER;

public class DemoNormalizedNER {
    public static void main(String[] args)
    {
        NormalizedNER nner = new NormalizedNER();
        String res1 = nner.getNormalizedNERinStr("五十刀");
        System.out.println(res1);
        String res2 = nner.getNormalizedNERinStr("五块钱");
        System.out.println(res2);
        String res3 = nner.getNormalizedNERinStr("五元钱");
        System.out.println(res3);
        String res4 = nner.getNormalizedNERinStr("五块二毛钱");
        System.out.println(res4);
        String res5 = nner.getNormalizedNERinStr("五元二角");
        System.out.println(res5);
        String res6 = nner.getNormalizedNERinStr("五美分");
        System.out.println(res6);
    }
}
