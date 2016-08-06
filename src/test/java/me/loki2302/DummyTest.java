package me.loki2302;

import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.TokenNameFinderFactory;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.*;
import opennlp.tools.tokenize.*;
import opennlp.tools.util.*;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DummyTest {
    @Test
    public void canCategorizeDocuments() throws IOException {
        List<DocumentSample> documentSampleList = new ArrayList<>();
        for(int i = 0; i < 3; ++i) { // Weirdly fails if there are too few samples: https://issues.apache.org/jira/browse/OPENNLP-488
            documentSampleList.add(new DocumentSample("greeting", "hello"));
            documentSampleList.add(new DocumentSample("greeting", "hello there"));
            documentSampleList.add(new DocumentSample("greeting", "hi"));
            documentSampleList.add(new DocumentSample("greeting", "hi there"));
            documentSampleList.add(new DocumentSample("greeting", "what's up"));
            documentSampleList.add(new DocumentSample("greeting", "hey"));
            documentSampleList.add(new DocumentSample("farewell", "good bye"));
            documentSampleList.add(new DocumentSample("farewell", "bye"));
            documentSampleList.add(new DocumentSample("farewell", "see ya"));
            documentSampleList.add(new DocumentSample("farewell", "see you later"));
            documentSampleList.add(new DocumentSample("farewell", "have a good day"));
            documentSampleList.add(new DocumentSample("farewell", "have a nice day"));
        }

        ObjectStream<DocumentSample> documentSampleObjectStream = new CollectionObjectStream<>(documentSampleList);
        DoccatModel doccatModel = DocumentCategorizerME.train(
                "en",
                documentSampleObjectStream,
                TrainingParameters.defaultParams(),
                new DoccatFactory());

        DocumentCategorizerME documentCategorizerME = new DocumentCategorizerME(doccatModel);

        if(true) {
            double[] outcomes = documentCategorizerME.categorize("hey there");
            String category = documentCategorizerME.getBestCategory(outcomes);
            assertEquals("greeting", category);
        }

        if(true) {
            double[] outcomes = documentCategorizerME.categorize("see ya later");
            String category = documentCategorizerME.getBestCategory(outcomes);
            assertEquals("farewell", category);
        }
    }

    @Test
    public void canFindNames() throws IOException {
        List<NameSample> nameSampleList = new ArrayList<>();
        for(int i = 0; i < 10; ++i) {
            nameSampleList.add(new NameSample(new String[]{"my", "name", "is", "joe"}, new Span[]{new Span(3, 4)}, false));
        }

        ObjectStream<NameSample> nameSampleObjectStream = new CollectionObjectStream<>(nameSampleList);
        TokenNameFinderModel tokenNameFinderModel = NameFinderME.train(
                "en",
                "person",
                nameSampleObjectStream,
                TrainingParameters.defaultParams(),
                new TokenNameFinderFactory());

        NameFinderME nameFinder = new NameFinderME(tokenNameFinderModel);

        String[] tokens = new String[] {"his", "name", "is", "john"};
        Span[] nameSpans = nameFinder.find(tokens);
        String[] names = Span.spansToStrings(nameSpans, tokens);
        assertEquals(1, names.length);
        assertEquals("john", names[0]);
        // assertEquals("person", nameSpans[0].getType()); // TODO: how do I make it work?
    }

    @Test
    public void canDetectSentences() throws IOException {
        List<SentenceSample> sentenceSampleList = new ArrayList<>();
            sentenceSampleList.add(new SentenceSample(
                    new DictionaryDetokenizer(
                            new DetokenizationDictionary(
                                    new String[] {
                                    },
                                    new DetokenizationDictionary.Operation[] {
                                    })),
                    new String[][] {
                            "My name is John .".split(" "),
                            "Today I woke up at 6 A.M.".split(" "),
                            "O.M.G.".split(" ")
                    }));

        ObjectStream<SentenceSample> sentenceSampleObjectStream = new CollectionObjectStream<>(sentenceSampleList);
        SentenceModel sentenceModel = SentenceDetectorME.train(
                "en",
                sentenceSampleObjectStream,
                new SentenceDetectorFactory("en", true, null, null),
                TrainingParameters.defaultParams());

        SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);
        String[] sentences = sentenceDetector.sentDetect("hello there. how are you?");
        assertEquals(2, sentences.length);
        assertEquals("hello there.", sentences[0]);
        assertEquals("how are you?", sentences[1]);
    }

    @Test
    public void canTokenize() throws IOException {
        List<TokenSample> tokenSampleList = new ArrayList<>();
        tokenSampleList.add(TokenSample.parse("test", "<SPACE>"));
        tokenSampleList.add(TokenSample.parse("test<SPACE>.", "<SPACE>"));
        tokenSampleList.add(TokenSample.parse("test<SPACE>!", "<SPACE>"));
        tokenSampleList.add(TokenSample.parse("test<SPACE>?", "<SPACE>"));
        tokenSampleList.add(TokenSample.parse("how<SPACE>is<SPACE>it<SPACE>going<SPACE>?", "<SPACE>"));

        ObjectStream<TokenSample> tokenSampleObjectStream = new CollectionObjectStream<>(tokenSampleList);

        TokenizerModel tokenizerModel = TokenizerME.train(
                tokenSampleObjectStream,
                new TokenizerFactory("en", null, true, null), // new TokenizerFactory() is supposed to be a default, but it doesn't work
                TrainingParameters.defaultParams());

        TokenizerME tokenizer = new TokenizerME(tokenizerModel);
        String[] tokens = tokenizer.tokenize("hello there! how are you?");
        System.out.println(Arrays.toString(tokens));
        assertEquals(7, tokens.length);
        assertEquals("hello", tokens[0]);
        assertEquals("there", tokens[1]);
        assertEquals("!", tokens[2]);
        assertEquals("how", tokens[3]);
        assertEquals("are", tokens[4]);
        assertEquals("you", tokens[5]);
        assertEquals("?", tokens[6]);
    }

    @Test
    public void canDetokenize() {
        DetokenizationDictionary detokenizationDictionary = new DetokenizationDictionary(
                new String[] {
                        "."
                },
                new DetokenizationDictionary.Operation[] {
                        DetokenizationDictionary.Operation.MOVE_LEFT
                });
        DictionaryDetokenizer dictionaryDetokenizer = new DictionaryDetokenizer(detokenizationDictionary);
        String detokenizedString = dictionaryDetokenizer.detokenize(
                new String[] {"Hello", "there", ".", "My", "name", "is", "John", "."}, "");

        assertEquals("Hello there. My name is John.", detokenizedString);
    }
}
