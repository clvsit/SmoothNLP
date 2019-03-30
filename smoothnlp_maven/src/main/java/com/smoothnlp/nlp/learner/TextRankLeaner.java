package com.smoothnlp.nlp.learner;
import com.google.gson.Gson;
import com.smoothnlp.nlp.SmoothNLP;
import com.hankcs.hanlp.seg.common.Term;
import com.smoothnlp.nlp.learner.BaseLearner;

import java.util.*;

/**
 * 代码的开发参考了Hanlp中TextRank的实现方式,
 * 但是对原有实现进行了适当拆解, 将建Trie与计算rankscore的部分分为两个部分,
 * 来适应BaseLearner中的fit和transform两步
 */

public class TextRankLeaner extends BaseLearner {

    /**
     * 阻尼系数（ＤａｍｐｉｎｇＦａｃｔｏｒ），一般取值为0.85
     */
    final static float d = 0.85f;
    /**
     * 最大迭代次数
     */
    public static int max_iter = 200;
    final static float min_diff = 0.001f;

    private boolean computed;
    private Map<String, Set<String>> words;
    private Map<String, Float> score;

    public TextRankLeaner(){
        this.computed = false;
        words = new TreeMap<String, Set<String>>();
        score = new HashMap<String, Float>();
    }

    public void fit(String inputText){
        String[] inputTokens = SmoothNLP.segment_pipeline.segmentText(inputText);
        this.buildTreeMap(inputTokens);
        this.computed = false;
    }

    public String transform(String inputText){
        if (!computed){
            this.computeTextRankScore();
        }
        String[] inputTokens = SmoothNLP.segment_pipeline.segmentText(inputText);
        int charIndex = 0;
        ArrayList<HashMap<String,String>> resList = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i< inputTokens.length; i++){
            HashMap<String,String> tokenRes = new HashMap<String, String>();
            tokenRes.put("token",inputTokens[i]);
            tokenRes.put("tokenIndex",Integer.toString(i));
            tokenRes.put("charStart",Integer.toString(charIndex));
            charIndex+=inputTokens[i].length();
            tokenRes.put("charEnd",Integer.toString(charIndex));
            if (score.containsKey(inputTokens[i])){
                tokenRes.put("textRankScore",Float.toString(score.get(inputTokens[i])));
            }
            resList.add(tokenRes);
        }
        Gson gsonobject = new Gson();
        return gsonobject.toJson(resList);
    }

    protected void buildTreeMap(String[] tokens){
        Queue<String> que = new LinkedList<String>();
        for (String w : tokens)
        {
            if (!words.containsKey(w))
            {
                words.put(w, new TreeSet<String>());
            }
            // 复杂度O(n-1)
            if (que.size() >= 5)
            {
                que.poll();
            }
            for (String qWord : que)
            {
                if (w.equals(qWord))
                {
                    continue;
                }
                //既然是邻居,那么关系是相互的,遍历一遍即可
                words.get(w).add(qWord);
                words.get(qWord).add(w);
            }
            que.offer(w);
        }
    }

    public void computeTextRankScore()
    {
        //依据TF来设置初值
        for (Map.Entry<String, Set<String>> entry : words.entrySet())
        {
            score.put(entry.getKey(), sigMoid(entry.getValue().size()));
        }
        for (int i = 0; i < max_iter; ++i)
        {
            Map<String, Float> m = new HashMap<String, Float>();
            float max_diff = 0;
            for (Map.Entry<String, Set<String>> entry : words.entrySet())
            {
                String key = entry.getKey();
                Set<String> value = entry.getValue();
                m.put(key, 1 - d);
                for (String element : value)
                {
                    int size = words.get(element).size();
                    if (key.equals(element) || size == 0) continue;
                    m.put(key, m.get(key) + d / size * (score.get(element) == null ? 0 : score.get(element)));
                }
                max_diff = Math.max(max_diff, Math.abs(m.get(key) - (score.get(key) == null ? 0 : score.get(key))));
            }
            score = m;
            if (max_diff <= min_diff) break;
        }
    }

    /**
     * sigmoid函数
     *
     * @param value
     * @return
     */
    public static float sigMoid(float value)
    {
        return (float) (1d / (1d + Math.exp(-value)));
    }

    public static void main(String[] args){
        String sampletext1 = "五块钱虽然买不到多少";
        String sampletext2 = "五块钱多少也是有价值的";
        TextRankLeaner tleaner = new TextRankLeaner();
        tleaner.fit(sampletext1);
        tleaner.fit(sampletext2);
        System.out.println(tleaner.transform(sampletext1));
    }

}
