package me.loki2302;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {
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
}
