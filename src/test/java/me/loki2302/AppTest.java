package me.loki2302;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class AppTest {
    @Test
    public void itShouldLoadValidPdf() throws IOException {
        PDDocument document;
        try(InputStream inputStream = AppTest.class.getResourceAsStream("/test.pdf")) {
            document = PDDocument.load(inputStream);
        }

        assertEquals(1, document.getNumberOfPages());
    }

    @Test
    public void itShouldThrowOnAttemptToLoadInvalidPdf() throws IOException {
        try(InputStream inputStream = AppTest.class.getResourceAsStream("/bad.pdf")) {
            try {
                PDDocument.load(inputStream);
                fail();
            } catch(IOException e) {
            }
        }
    }

    @Test
    public void itShouldLetMeExtractText() throws IOException {

        PDDocument document;
        try(InputStream inputStream = AppTest.class.getResourceAsStream("/test.pdf")) {
            document = PDDocument.load(inputStream);
        }

        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        String text = pdfTextStripper.getText(document);
        assertTrue(text.contains("This is a title"));
        assertTrue(text.contains("And this is some dummy text."));
    }

}
