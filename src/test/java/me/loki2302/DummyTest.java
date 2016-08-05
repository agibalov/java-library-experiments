package me.loki2302;

import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.util.CollectionObjectStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.TrainingParameters;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
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
}
