package me.loki2302;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Test;
import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AppTest {
    @Test
    public void dummy() throws IOException {
        PDDocument pdDocument = PDDocument.load(App.class.getClassLoader().getResource("funds.pdf"));
        assertEquals(1, pdDocument.getNumberOfPages());

        ObjectExtractor objectExtractor = new ObjectExtractor(pdDocument);

        PageIterator it = objectExtractor.extract();
        List<Page> pages = new ArrayList<>();
        while(it.hasNext()) {
            Page page = it.next();
            pages.add(page);
        }

        assertEquals(1, pages.size());

        Page theOnlyPage = pages.get(0);
        assertEquals(1, theOnlyPage.getPageNumber());

        SpreadsheetExtractionAlgorithm spreadsheetExtractionAlgorithm =
                new SpreadsheetExtractionAlgorithm();
        List<? extends Table> tables = spreadsheetExtractionAlgorithm.extract(theOnlyPage);
        assertEquals(1, tables.size());

        Table theOnlyTable = tables.get(0);
        assertEquals(5, theOnlyTable.getRows().size());
        assertEquals(2, theOnlyTable.getCols().size());

        assertCell("Account", theOnlyTable, 0, 0); assertCell("Amount", theOnlyTable, 0, 1);
        assertCell("My pocket", theOnlyTable, 1, 0); assertCell("$100.00", theOnlyTable, 1, 1);
        assertCell("My wallet", theOnlyTable, 2, 0); assertCell("$200.00", theOnlyTable, 2, 1);
        assertCell("", theOnlyTable, 3, 0); assertCell("", theOnlyTable, 3, 1);
        assertCell("Total", theOnlyTable, 4, 0); assertCell("$300.00", theOnlyTable, 4, 1);
    }

    private static void assertCell(String expected, Table table, int row, int column) {
        assertEquals(expected, table.getCell(row, column).getText());
    }
}
