package com.smoothnlp.nlp.util;

import java.util.*;

public class ConvertingFunctions {

    public static HashMap<String,String> doubleHashMao2strHashMao(HashMap<String,Double> dmap){
        HashMap<String,String> smap = new HashMap<String, String>();
        for (String k: dmap.keySet()){
            smap.put(k,Double.toString(dmap.get(k)));
        }
        return smap;
    }

    public static HashMap<String,String> intHashMao2strHashMao(HashMap<String,Integer> imap){
        HashMap<String,String> smap = new HashMap<String, String>();
        for (String k: imap.keySet()){
            smap.put(k,Double.toString(imap.get(k)));
        }
        return smap;
    }

    public static HashMap<String,Double> strHashMap2doubleHashMap(HashMap<String,String> smap){
        HashMap<String,Double> dmap = new HashMap<String, Double>();
        for (String k: smap.keySet()){
            dmap.put(k,Double.parseDouble(smap.get(k)));
        }
        return dmap;
    }

}
