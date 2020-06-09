package io.agibalov;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class DummyTest {
    @Test
    public void canWriteCsv() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(baos);

        CSVPrinter printer = new CSVPrinter(osw, CSVFormat.DEFAULT.withHeader("xxx", "yyy"));
        printer.printRecord("abc", 123);
        printer.flush();

        assertEquals("xxx,yyy\r\nabc,123\r\n", baos.toString());
    }

    @Test
    public void canReadCsv() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream("xxx,yyy\r\nabc,123\r\ndef,234\r\n".getBytes(StandardCharsets.UTF_8));
        InputStreamReader isr = new InputStreamReader(bais);
        CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(isr);
        List<Map<String, String>> rows = new ArrayList<>();
        for (CSVRecord record : parser) {
            rows.add(record.toMap());
        }
        assertEquals(Arrays.asList(
                new HashMap<String, String>() {{
                    put("xxx", "abc");
                    put("yyy", "123");
                }},
                new HashMap<String, String>() {{
                    put("xxx", "def");
                    put("yyy", "234");
                }}
        ), rows);
    }
}
