package com.smoothnlp.nlp.pipeline;

import com.smoothnlp.nlp.SmoothNLP;
import com.smoothnlp.nlp.basic.SEntity;
import com.smoothnlp.nlp.basic.SToken;
import com.smoothnlp.nlp.basic.UtilFns;

import java.util.*;

public class MultiNersPipeline extends BaseEntityRecognizer {

    private BaseEntityRecognizer[] nerPipelines;

    public MultiNersPipeline(BaseEntityRecognizer[]  nerPipelines){
        this.nerPipelines = nerPipelines;
    }

    @Override
    public List<SEntity> process(List<SToken> sTokenList) {
        List<SEntity> entityList = new LinkedList<>();
        for (BaseEntityRecognizer ner: this.nerPipelines){
            entityList.addAll(ner.process(sTokenList));
        }
        entityList = deDupOverlapedEntity(entityList);
        return entityList;
    }

    @Override
    public List<SEntity> process(String inputText) {
        List<SEntity> entityList = new LinkedList<>();
        for (BaseEntityRecognizer ner: this.nerPipelines){
            entityList.addAll(ner.process(inputText));
        }
        entityList = deDupOverlapedEntity(entityList);
        return entityList;
    }

    public List<SEntity> deDupOverlapedEntity(List<SEntity> entities){
        List<SEntity> deDupledList = new LinkedList<>();
        PriorityQueue<SEntity> pqEntities = new PriorityQueue<>();
        for (SEntity entity : entities){
            System.out.println("add to queue: "+entity.text);
            pqEntities.add(entity);
        }
        List<int[]> trackedRanges = new LinkedList<>();
        while(!pqEntities.isEmpty()){
            SEntity en = pqEntities.poll();
            System.out.print("Entity: ");
            System.out.println(en);
            boolean entityOverlaped = false;
            for (int[] range: trackedRanges){
                if (en.charStart>=range[0] & en.charEnd<=range[1]){
                    entityOverlaped = true;
                    break;
                }
            }
            if (!entityOverlaped){
                int[] newRange ={en.charStart,en.charEnd};
                System.out.print("added range: ");
                System.out.println(UtilFns.toJson(newRange));
                trackedRanges.add(newRange);
                deDupledList.add(en);
            }
        }


        return deDupledList;

    }

    public static void main(String[] args){
        MultiNersPipeline mner = new MultiNersPipeline(new BaseEntityRecognizer[]{SmoothNLP.CRF_NER,SmoothNLP.REGEX_NER});
        System.out.println(UtilFns.toJson(mner.process("腾讯科技与上海文磨达成协议")));
    }

}
