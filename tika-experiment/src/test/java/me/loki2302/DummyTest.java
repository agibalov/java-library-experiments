package me.loki2302;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DummyTest {
    @Test
    public void canParseDoc() throws IOException, TikaException {
        Tika tika = new Tika();
        try(InputStream is = DummyTest.class.getResourceAsStream("/test.doc")) {
            String s = tika.parseToString(is).trim();
            assertTrue(s.contains("Dummy article"));
            assertTrue(s.contains("This article does not make any sense at all."));
            assertTrue(s.contains("And that\u2019s pretty much it."));
        }
    }

    @Test
    public void canParseDocx() throws IOException, TikaException {
        Tika tika = new Tika();
        try(InputStream is = DummyTest.class.getResourceAsStream("/test.docx")) {
            String s = tika.parseToString(is).trim();
            assertTrue(s.contains("Dummy article"));
            assertTrue(s.contains("This article does not make any sense at all."));
            assertTrue(s.contains("And that\u2019s pretty much it."));
        }
    }

    @Test
    public void canParseHtml() throws IOException, TikaException {
        Tika tika = new Tika();
        try(InputStream is = DummyTest.class.getResourceAsStream("/test.html")) {
            String s = tika.parseToString(is).trim();
            System.out.println(s);
            assertTrue(s.contains("Dummy\narticle"));
            assertTrue(s.contains("This\narticle does not make any sense at all."));
            assertTrue(s.contains("And that\u2019s pretty\nmuch it."));
        }
    }

    @Test
    public void canParseOdt() throws IOException, TikaException {
        Tika tika = new Tika();
        try(InputStream is = DummyTest.class.getResourceAsStream("/test.odt")) {
            String s = tika.parseToString(is).trim();
            assertTrue(s.contains("Dummy article"));
            assertTrue(s.contains("This article does not make any sense at all."));
            assertTrue(s.contains("And that\u2019s pretty much it."));
        }
    }

    @Test
    public void canParsePdf() throws IOException, TikaException {
        Tika tika = new Tika();
        try(InputStream is = DummyTest.class.getResourceAsStream("/test.pdf")) {
            String s = tika.parseToString(is).trim();
            assertTrue(s.contains("Dummy article"));
            assertTrue(s.contains("This article does not make any sense at all."));
            assertTrue(s.contains("And that\u2019s pretty much it."));
        }
    }

    @Test
    public void canParseRtf() throws IOException, TikaException {
        Tika tika = new Tika();
        try(InputStream is = DummyTest.class.getResourceAsStream("/test.rtf")) {
            String s = tika.parseToString(is).trim();
            assertTrue(s.contains("Dummy article"));
            assertTrue(s.contains("This article does not make any sense at all."));
            assertTrue(s.contains("And that\u2019s pretty much it."));
        }
    }

    @Test
    public void canParseTxt() throws IOException, TikaException {
        Tika tika = new Tika();
        try(InputStream is = DummyTest.class.getResourceAsStream("/test.txt")) {
            String s = tika.parseToString(is).trim();
            assertTrue(s.contains("Dummy article"));
            assertTrue(s.contains("This article does not make any sense at all."));
            assertTrue(s.contains("And that\u2019s pretty much it."));
        }
    }
}
