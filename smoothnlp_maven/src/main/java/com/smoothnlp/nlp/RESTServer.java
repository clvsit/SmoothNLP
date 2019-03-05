package com.smoothnlp.nlp;
import edu.stanford.nlp.pipeline.StanfordCoreNLPServer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

public class RESTServer {
    public static void main(String[] args) throws IOException {
        System.out.println("I am a remodified CoreNLP Server");
        String[] defaultArgs = new String[]{"-props","StanfordCoreNLP-chinese.properties","-port","9000"};
        if (args.length<=2){
            StanfordCoreNLPServer.main(defaultArgs);
        }else{
            List<String> argList = Arrays.asList(args);
            if (argList.contains("-props")){
                StanfordCoreNLPServer.main(args);
            }else{
                String[] argsCombined = (String[])ArrayUtils.addAll(new String[]{"-props","StanfordCoreNLP-chinese.properties"}, args);
                StanfordCoreNLPServer.main(argsCombined);
            }
        }
    }
}
