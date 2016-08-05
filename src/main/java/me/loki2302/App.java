package me.loki2302;

import opennlp.tools.doccat.*;
import opennlp.tools.util.CollectionObjectStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.TrainingParameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {
        List<DocumentSample> documentSampleList = new ArrayList<>();
        for(int i = 0; i < 3; ++i) {
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
        double[] outcomes = documentCategorizerME.categorize("hey there");
        for(int i = 0; i < outcomes.length; ++i) {
            String category = documentCategorizerME.getCategory(i);
            System.out.printf("%s: %f\n", category, outcomes[i]);
        }
    }
}
