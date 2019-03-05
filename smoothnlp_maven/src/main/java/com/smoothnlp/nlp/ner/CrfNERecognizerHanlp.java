package com.smoothnlp.nlp.ner;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.model.crf.CRFNERecognizer;
import com.hankcs.hanlp.model.crf.LogLinearModel;
import com.hankcs.hanlp.model.crf.crfpp.Encoder;
import com.hankcs.hanlp.model.crf.crfpp.crf_learn;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.model.perceptron.tagset.NERTagSet;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;

public class CrfNERecognizerHanlp extends CRFNERecognizer{

    private static String modelPath = "/Users/ruinanzhang/Desktop/Sync/smoothnlp/ner/model/hanlp_crf_ner.bin";
    private static String trainCorpusPath = "/Users/ruinanzhang/Desktop/Sync/smoothnlp/ner/data/sample_train.txt";
    private static String renminRiBao = "/Users/ruinanzhang/Downloads/pku98/199801-test.txt";
    private NERTagSet tagSet = new NERTagSet();

    public CrfNERecognizerHanlp(String modelPath) throws IOException
    {
        super(modelPath);
    }

    public CrfNERecognizerHanlp() throws IOException
    {
        super(HanLP.Config.CRFNERModelPath);
    }

    public void addNERLabels(String newNerTag)
    {
        tagSet.nerLabels.add(newNerTag);
    }

    @Override
    public NERTagSet getNERTagSet()
    {
        return tagSet;
    }

    private void convert(String modelFile) throws IOException
    {
        this.model = new LogLinearModel(modelFile + ".txt", modelFile);
    }

    protected String getMSRAFeatureTemplate()
    {
        return "# Unigram\n" +
                // form
                "U0:%x[-2,0]\n" +
                "U1:%x[-1,0]\n" +
                "U2:%x[0,0]\n" +
                "U3:%x[1,0]\n" +
                "U4:%x[2,0]\n" +
                "\n" +
                "# Bigram\n" +
                "B";
    }

    @Override
    public void train(String trainCorpusPath, String modelPath) throws IOException
    {
        crf_learn.Option option = new crf_learn.Option();
        train(trainCorpusPath, modelPath, option.maxiter, option.freq, option.eta, option.cost,
                option.thread, option.shrinking_size, Encoder.Algorithm.fromString(option.algorithm));
    }

    @Override
    public void train(String trainFile, String modelFile,
                      int maxitr, int freq, double eta, double C, int threadNum, int shrinkingSize,
                      Encoder.Algorithm algorithm) throws IOException
    {
        String templFile = null;
        File tmpTemplate = File.createTempFile("crfpp-template-" + new Date().getTime(), ".txt");
        tmpTemplate.deleteOnExit();
        templFile = tmpTemplate.getAbsolutePath();
        String template = getDefaultFeatureTemplate();
        IOUtil.saveTxt(templFile, template);

        // create template file
        File tmpTrain = File.createTempFile("crfpp-train-" + new Date().getTime(), ".txt");
//        tmpTrain.deleteOnExit();

        // convert corpus to template file
        convertCorpus(trainFile, tmpTrain.getAbsolutePath());

        // get absolute address
        trainFile = tmpTrain.getAbsolutePath();
        System.out.println("this is train file:");
        System.out.println(trainFile);
        System.out.printf("Java效率低，建议安装CRF++，执行下列等价训练命令（不要终止本进程，否则临时语料库和特征模板将被清除）：\n" +
                        "crf_learn -m %d -f %d -e %f -c %f -p %d -H %d -a %s -t %s %s %s\n", maxitr, freq, eta,
                C, threadNum, shrinkingSize, algorithm.toString().replace('_', '-'),
                templFile, trainFile, modelFile);
        Encoder encoder = new Encoder();
        if (!encoder.learn(templFile, trainFile, modelFile,
                true, maxitr, freq, eta, C, threadNum, shrinkingSize, algorithm))
        {
            throw new IOException("fail to learn model");
        }
        this.convert(modelFile);
    }

    public void train(String trainFile,String modelFile, String templateFtrs) throws IOException
    {
        String templFile = null;
        File tmpTemplate = File.createTempFile("crfpp-template-" + new Date().getTime(), ".txt");
        tmpTemplate.deleteOnExit();
        templFile = tmpTemplate.getAbsolutePath();
        String template = getDefaultFeatureTemplate();
        IOUtil.saveTxt(templFile, template);

        Encoder encoder = new Encoder();
        encoder.learn(templFile,trainFile,modelFile,true,
                10,1,0.0001,1,1 ,20,
                Encoder.Algorithm.fromString("CRF-L2"));
        convert(modelFile);
    }

    public static void main(String[] args) throws IOException{

//        Options options = new Options();
//        Option trainFile = new Option("trainFile",true,"input Train Corpus");
//        trainFile.setRequired(true);
//        options.addOption(trainFile);

        // train renMin
//        CrfNERecognizerHanlp crfner = new CrfNERecognizerHanlp(null);
//        crfner.addNERLabels("nr");
//        crfner.addDefaultNERLabels();
//        System.out.println(crfner.getNERTagSet().nerLabels.toString());
//        System.out.println(crfner.tagSet.nerLabels.toString());
//        crfner.train(renminRiBao,modelPath);

        CrfNERecognizerHanlp crfner = new CrfNERecognizerHanlp(null);
//        crfner.addNERLabels("person_name");
        String MSRATempalte = crfner.getMSRAFeatureTemplate();
        crfner.train(renminRiBao,modelPath);

//        CRFNERecognizer crfner = new CRFNERecognizer(null);
////        crfner.train("/Users/ruinanzhang/Downloads/pku98/199801.txt","/Users/ruinanzhang/Desktop/Sync/smoothnlp/crfner_hanlp.bin");
//        crf_learn.Option option = new crf_learn.Option();
//        Encoder encoder = new Encoder();
//
//        String templFile = null;
//        File tmpTemplate = File.createTempFile("crfpp-template-" + new Date().getTime(), ".txt");
//        tmpTemplate.deleteOnExit();
//        templFile = tmpTemplate.getAbsolutePath();
//
////        String template = crfner.getDefaultFeatureTemplate();
////        IOUtil.saveTxt(templFile, template);
//
//        Boolean learnedFlag = encoder.learn(templFile,trainCorpusPath,modelPath,
//                false,20,1,0.0001, 1,
//                1,20,Encoder.Algorithm.fromString("CRF-L2"));
//        if (!learnedFlag){
//            throw new IOException("fialed to learn model");
//        }
    }
}
