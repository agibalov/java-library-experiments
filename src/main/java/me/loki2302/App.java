package me.loki2302;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.ext.awt.image.spi.ImageWriter;
import org.apache.batik.ext.awt.image.spi.ImageWriterRegistry;
import org.apache.batik.svggen.DefaultExtensionHandler;
import org.apache.batik.svggen.ImageHandlerBase64Encoder;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {
        extractText();
        renderToPng();
        renderToSvg();
    }

    private static void extractText() throws IOException {
        PDDocument document;
        try(InputStream inputStream = App.class.getResourceAsStream("/test.pdf")) {
            document = PDDocument.load(inputStream);
        }

        int pageCount = document.getNumberOfPages();
        System.out.printf("pages=%d\n", pageCount);

        PDFTextStripper pdfTextStripper = new PDFTextStripper() {
            @Override
            protected void startArticle() throws IOException {
                super.startArticle();
                System.out.printf("start article\n");
            }

            @Override
            protected void endArticle() throws IOException {
                super.endArticle();
                System.out.printf("end article\n");
            }

            @Override
            protected void writeParagraphSeparator() throws IOException {
                super.writeParagraphSeparator();
                System.out.printf("paragraph separator\n");
            }

            @Override
            protected void writeParagraphStart() throws IOException {
                super.writeParagraphStart();
                System.out.printf("paragraph start\n");
            }

            @Override
            protected void writeParagraphEnd() throws IOException {
                super.writeParagraphEnd();
                System.out.printf("paragraph end\n");
            }

            @Override
            protected void writeLineSeparator() throws IOException {
                super.writeLineSeparator();
                System.out.printf("line separator\n");
            }

            @Override
            protected void writeWordSeparator() throws IOException {
                super.writeWordSeparator();
                System.out.printf("word separator\n");
            }
            @Override
            protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                super.writeString(text, textPositions);

                StringBuilder sb = new StringBuilder();
                for(TextPosition textPosition : textPositions) {
                    sb.append(String.format("[%s](%1.0f) ", textPosition.getUnicode(), textPosition.getFontSize()));
                }

                System.out.printf("string=%s < %s >\n", text, sb);

            }
        };
        String text = pdfTextStripper.getText(document);
        System.out.println(text);
    }

    private static void renderToPng() throws IOException {
        PDDocument document;
        try(InputStream inputStream = App.class.getResourceAsStream("/test.pdf")) {
            document = PDDocument.load(inputStream);
        }

        PDFRenderer pdfRenderer = new PDFRenderer(document);
        BufferedImage bufferedImage = pdfRenderer.renderImage(0);
        ImageIO.write(bufferedImage, "png", new File("page0.png"));
    }

    private static void renderToSvg() throws IOException {
        PDDocument pdfDocument;
        try(InputStream inputStream = App.class.getResourceAsStream("/test.pdf")) {
            pdfDocument = PDDocument.load(inputStream);
        }

        DOMImplementation domImplementation = GenericDOMImplementation.getDOMImplementation();

        Document svgDocument = domImplementation.createDocument(
                "http://www.w3.org/2000/svg",
                "svg",
                null);

        SVGGraphics2D svgGraphics2d = new SVGGraphics2D(svgDocument);

        PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);
        pdfRenderer.renderPageToGraphics(0, svgGraphics2d);

        svgGraphics2d.stream("page0.svg");
    }
}
