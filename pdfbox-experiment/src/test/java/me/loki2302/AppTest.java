package me.loki2302;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.ext.awt.image.spi.ImageWriter;
import org.apache.batik.ext.awt.image.spi.ImageWriterRegistry;
import org.apache.batik.svggen.DefaultExtensionHandler;
import org.apache.batik.svggen.ImageHandlerBase64Encoder;
import org.apache.batik.svggen.ImageHandlerPNGEncoder;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Test;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
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
