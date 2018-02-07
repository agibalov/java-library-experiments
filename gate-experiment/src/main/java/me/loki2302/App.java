package me.loki2302;

import gate.*;
import gate.creole.SerialAnalyserController;
import gate.creole.gazetteer.DefaultGazetteer;
import gate.creole.splitter.SentenceSplitter;
import gate.creole.tokeniser.DefaultTokeniser;
import gate.util.GateException;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Iterator;

public class App {
    public static void main(String[] args) throws GateException, MalformedURLException {
        Gate.setGateHome(Paths.get("/home/loki2302/GATE81").toFile());
        Gate.init();

        System.out.printf("plugins home: %s (%d known plugins)\n", Gate.getPluginsHome(), Gate.getKnownPlugins().size());
        for(URL knownPluginUrl : Gate.getKnownPlugins()) {
            System.out.printf("  known plugin: %s\n", knownPluginUrl);
        }

        Gate.getCreoleRegister().registerDirectories(Paths.get(Gate.getPluginsHome().getAbsolutePath(), "ANNIE").toUri().toURL());

        Corpus corpus = Factory.newCorpus("Dummy corpus");
        Document document = Factory.newDocument("Hello there! My name is John Smith and I like cats.");
        corpus.add(document);

        System.out.printf("corpus size: %d\n", corpus.size());

        SerialAnalyserController serialAnalyserController = (SerialAnalyserController)Factory.createResource("gate.creole.SerialAnalyserController");

        // adds Token, SpaceToken
        DefaultTokeniser defaultTokeniser = (DefaultTokeniser)Factory.createResource("gate.creole.tokeniser.DefaultTokeniser");
        serialAnalyserController.add(defaultTokeniser);

        // adds Sentence, Split
        SentenceSplitter sentenceSplitter = (SentenceSplitter)Factory.createResource("gate.creole.splitter.SentenceSplitter");
        serialAnalyserController.add(sentenceSplitter);

        // add Lookup
        //DefaultGazetteer defaultGazetteer = (DefaultGazetteer)Factory.createResource("gate.creole.gazetteer.DefaultGazetteer");
        DefaultGazetteer defaultGazetteer = new DefaultGazetteer();
        defaultGazetteer.setListsURL(App.class.getClassLoader().getResource("lookup-index.def"));
        defaultGazetteer.setGazetteerFeatureSeparator("&");
        defaultGazetteer.init();
        serialAnalyserController.add(defaultGazetteer);

        serialAnalyserController.setCorpus(corpus);
        serialAnalyserController.execute();

        Iterator documentIterator = corpus.iterator();
        while(documentIterator.hasNext()) {
            Document doc = (Document) documentIterator.next();
            System.out.printf("document: %s\n", doc);

            System.out.printf("  annotations:\n");
            AnnotationSet annotations = doc.getAnnotations();
            for(Annotation annotation : annotations) {
                long startOffset = annotation.getStartNode().getOffset();
                long endOffset = annotation.getEndNode().getOffset();
                DocumentContent annotationContent = doc.getContent().getContent(startOffset, endOffset);

                System.out.printf("    annotation: %s (%s)\n", annotation.getType(), annotationContent);

                FeatureMap featureMap = annotation.getFeatures();
                Iterator featureIterator = featureMap.keySet().iterator();
                while(featureIterator.hasNext()) {
                    String featureKey = (String) featureIterator.next();
                    System.out.printf("      feature '%s': %s\n", featureKey, featureMap.get(featureKey));
                }
            }

            System.out.printf("  features:\n");
            FeatureMap featureMap = doc.getFeatures();
            Iterator featureIterator = featureMap.keySet().iterator();
            while(featureIterator.hasNext()) {
                String featureKey = (String) featureIterator.next();
                System.out.printf("    feature '%s': %s\n", featureKey, featureMap.get(featureKey));
            }
        }
    }
}
