package me.loki2302;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import java.io.IOException;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) throws IOException, TikaException {
        Tika tika = new Tika();

        if(true) {
            String s = tika.parseToString(Paths.get("/home/loki2302/tika-experiment/test.pdf"));
            System.out.println(s.trim());
        }

        if(true) {
            String s = tika.parseToString(Paths.get("/home/loki2302/tika-experiment/test.docx"));
            System.out.println(s.trim());
        }
    }
}
