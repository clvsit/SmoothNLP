package com.smoothnlp.nlp;

import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.ArgumentParser;

import java.io.IOException;
import java.util.Properties;

import edu.stanford.nlp.util.StringUtils;

public class Main {
    public static void main(String[] args) throws IOException {

        ArgumentParser.fillOptions(StanfordCoreNLPServer.class, args);
        // get server properties from command line, right now only property used is server_id
        Properties serverProperties = StringUtils.argsToProperties(args);
        StanfordCoreNLPServer server = new StanfordCoreNLPServer(serverProperties);  // must come after filling global options
        ArgumentParser.fillOptions(server, args);
        // align status port and server port in case status port hasn't been set and
        // server port is not the default 9000
//        if (serverProperties != null && !serverProperties.containsKey("status_port") &&
//                serverProperties.containsKey("port")) {
//            server.statusPort = Integer.parseInt(serverProperties.getProperty("port"));
//        }

//        StanfordCoreNLP pipeline = new StanfordCoreNLP(
//                PropertiesUtils.asProperties(
//                        "annotators", "tokenize,ssplit,pos,lemma,parse,natlog",
//                        "ssplit.isOneSentence", "true",
//                        "parse.model", "edu/stanford/nlp/models/srparser/englishSR.ser.gz",
//                        "tokenize.language", "en"));

    }
}
