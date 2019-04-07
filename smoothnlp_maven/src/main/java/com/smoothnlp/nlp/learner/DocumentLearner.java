package com.smoothnlp.nlp.learner;

import java.util.HashMap;
import com.google.gson.Gson;
import com.smoothnlp.nlp.learner.money.MoneyLearner;


public class DocumentLearner implements BaseLearner {

    public static HashMap<String, BaseLearner> subLearners;

    public DocumentLearner(){
        this.subLearners = new HashMap<String, BaseLearner>();
        subLearners.put("TokenLearners", new DocumentTokenLearner());
        subLearners.put("MoneyLearners", new MoneyLearner());
    }

    public void fit(String inputText){
        for (String learnerName : subLearners.keySet()){
            subLearners.get(learnerName).fit(inputText);
        }
    }

    public void fit(String[] inputCorpus){
        for (String learnerName : subLearners.keySet()){
            subLearners.get(learnerName).fit(inputCorpus);
        }
    }

    public String transform(String inputText){
        HashMap<String,String> documentRes = new HashMap<String, String>();
        for (String learnerName : subLearners.keySet()){
            String learnerResItem = subLearners.get(learnerName).transform(inputText);
            documentRes.put(learnerName,learnerResItem);
        }
        Gson gson = new Gson();
        return gson.toJson(documentRes);
    }

    public class DocumentTokenLearner extends TokenLearner {

        private TokenLearner[] tokenSubLearners;

        public DocumentTokenLearner(){
            super();
            this.tokenSubLearners = new TokenLearner[]{
                    new PMIEntropyLearner(),
                    new TextRankLeaner()
            };
        }

        @Override
        public void fit(String inputText){
            for (TokenLearner l : this.tokenSubLearners){
                l.fit(inputText);
            }
            this.fitted = false;
        }

        @Override
        protected void compute(){
            for (TokenLearner l : this.tokenSubLearners){
                l.compute();
            }
        }

        @Override
        public HashMap<String,String> encodeToken(String token){
            HashMap<String,String> tokenRes = new HashMap<String,String>();
            for (TokenLearner l : this.tokenSubLearners){
                tokenRes.putAll(l.encodeToken(token));
            }
            return tokenRes;
        }
    }

    public static void main(String[] args){
        String sampletext1 = "今天天气不错,天气这么好, 真应该出去玩耍呀";
        String sampletext2 = "今天天气不错, 但是如果不出去玩, 在家里写代码也会很开心";
        DocumentLearner pleaner = new DocumentLearner();
        pleaner.fit(sampletext1);
        pleaner.fit(sampletext2);
        System.out.println(pleaner.transform(sampletext1));
    }

}

