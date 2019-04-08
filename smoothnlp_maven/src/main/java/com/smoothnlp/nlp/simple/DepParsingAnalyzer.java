package com.smoothnlp.nlp.simple;
import com.smoothnlp.nlp.SmoothNLP;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import java.util.HashMap;
import java.util.ArrayList;
import com.google.gson.Gson;

import com.hankcs.hanlp.seg.Viterbi.ViterbiSegment;

import com.hankcs.hanlp.dependency.nnparser.NeuralNetworkDependencyParser;

public class DepParsingAnalyzer implements SimplePipeline {

    private String mode;

    public DepParsingAnalyzer(){
        this.mode = SmoothNLP.DEPPARSE_MODE;
    }

    public DepParsingAnalyzer(String mode){
        this.mode = mode;
    }

    public String analyze(String inputText){
        if (this.mode == SmoothNLP.DEPPARSE_MODE){
            Gson gsonobject = new Gson();
            return gsonobject.toJson(this.hanlpNeuralDParse(inputText));
        }
        return "";
    }

    public ArrayList<HashMap<String,String>> hanlpNeuralDParse(String inputText){

        ArrayList<HashMap<String,String>> resList = new ArrayList<HashMap<String,String>>();

        NeuralNetworkDependencyParser neuralNetworkDependencyParser = new NeuralNetworkDependencyParser(new ViterbiSegment());

        CoNLLSentence sentence = neuralNetworkDependencyParser.parse(inputText);
        CoNLLWord[] wordArray = sentence.getWordArray();

        ArrayList<HashMap<String,String>> seg_reslist = SmoothNLP.segment_pipeline.analyzeRaw(inputText);
        HashMap<String,HashMap<String,String>> tokenIndex2segInfos = new HashMap<String, HashMap<String, String>>();
        for (HashMap<String,String> seg_res : seg_reslist){
            tokenIndex2segInfos.put(seg_res.get("tokenIndex"),seg_res);
        }

        for (int i = wordArray.length - 1; i >= 0; i--)
        {
            HashMap<String,String> pairRes = new HashMap<String, String>();
            CoNLLWord word = wordArray[i];
            pairRes.put("targetToken",word.LEMMA);
            pairRes.put("relationship",word.DEPREL);
            pairRes.put("sourceToken",word.HEAD.LEMMA);
            pairRes.put("targetTokenIndex",Integer.toString(word.ID-1));
            pairRes.put("sourceTokenIndex",Integer.toString(word.HEAD.ID-1));

            if (word.HEAD.ID==0 | word.ID==0){
                // pass when source is the dummy root of dependency parsing tree
                continue;
            }

            pairRes.put("targetCharStart",tokenIndex2segInfos.get(pairRes.get("targetTokenIndex")).get("charStart"));
            pairRes.put("targetCharEnd",tokenIndex2segInfos.get(pairRes.get("targetTokenIndex")).get("charEnd"));
            pairRes.put("sourceCharStart",tokenIndex2segInfos.get(pairRes.get("sourceTokenIndex")).get("charStart"));
            pairRes.put("sourceCharEnd",tokenIndex2segInfos.get(pairRes.get("sourceTokenIndex")).get("charEnd"));

            resList.add(pairRes);
        }
        return resList;
    }

    public static ArrayList<HashMap<String,String>> getDependenciesByCharRange(ArrayList<HashMap<String,String>> dp_reslist,int charStart,int charEnd){
        return getDependenciesByCharRange(dp_reslist,charStart,charEnd,true);
    }

    public static ArrayList<HashMap<String,String>> getDependenciesByCharRange(ArrayList<HashMap<String,String>> dp_reslist,int charStart,int charEnd, boolean target_only){
        ArrayList<HashMap<String,String>> resList = new ArrayList<HashMap<String,String>>();
        for (HashMap<String,String> dpRes : dp_reslist){
            int sourceCharStart = Integer.valueOf(dpRes.get("sourceCharStart"));
            int sourceCharEnd = Integer.valueOf(dpRes.get("sourceCharEnd"));
            int targetCharStart = Integer.valueOf(dpRes.get("targetCharStart"));
            int targetCharEnd = Integer.valueOf(dpRes.get("targetCharEnd"));

            boolean target_condition = (targetCharStart >= charStart & targetCharEnd-1 <= charEnd);
            boolean source_condition = (sourceCharStart >= charStart & sourceCharEnd-1 <= charEnd);

            // 过滤区间间的dependencies
            if (target_condition & source_condition){ continue; }

            if (target_only){
                if (target_condition){
                    resList.add(dpRes);
                }
            }else{
                if (source_condition){
                    resList.add(dpRes);
                }
            }


        }

        return resList;
    }

    public static void main(String[] args){
        System.out.println("Hello, I am DepParser");
        DepParsingAnalyzer dparser = new DepParsingAnalyzer();
        System.out.println(dparser.analyze("我买了五斤苹果, 总共10元"));
        ArrayList<HashMap<String,String>> dpres_list = dparser.hanlpNeuralDParse("我买了五斤苹果, 总共10元");
        System.out.println(getDependenciesByCharRange(dpres_list,11,14,false).size());
//        CoNLLSentence sentence = HanLP.parseDependency("徐先生还具体帮助他确定了把画雄鹰、松鼠和麻雀作为主攻目标。");
//        System.out.println(sentence);
//        // 可以方便地遍历它
//        for (CoNLLWord word : sentence)
//        {
//            System.out.printf("%s --(%s)--> %s\n", word.LEMMA, word.DEPREL, word.HEAD.LEMMA);
//        }
//        // 也可以直接拿到数组，任意顺序或逆序遍历
//        CoNLLWord[] wordArray = sentence.getWordArray();
//        for (int i = wordArray.length - 1; i >= 0; i--)
//        {
//            CoNLLWord word = wordArray[i];
//            System.out.printf("%s --(%s)--> %s\n", word.LEMMA, word.DEPREL, word.HEAD.LEMMA);
//        }
//        // 还可以直接遍历子树，从某棵子树的某个节点一路遍历到虚根
//        CoNLLWord head = wordArray[12];
//        while ((head = head.HEAD) != null)
//        {
//            if (head == CoNLLWord.ROOT) System.out.println(head.LEMMA);
//            else System.out.printf("%s --(%s)--> ", head.LEMMA, head.DEPREL);
//        }

    }

}
