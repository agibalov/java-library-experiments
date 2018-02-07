package me.loki2302;

import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.IOException;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {
        PDDocument pdDocument = PDDocument.load(App.class.getClassLoader().getResource("funds.pdf"));
        System.out.printf("%d\n", pdDocument.getNumberOfPages());

        ObjectExtractor objectExtractor = new ObjectExtractor(pdDocument);
        PageIterator it = objectExtractor.extract();
        while(it.hasNext()) {
            Page page = it.next();
            System.out.printf("page %d\n", page.getPageNumber());

            SpreadsheetExtractionAlgorithm spreadsheetExtractionAlgorithm =
                    new SpreadsheetExtractionAlgorithm();
            List<? extends Table> tables = spreadsheetExtractionAlgorithm.extract(page);
            System.out.printf("tables=%d\n", tables.size());
            for(Table table : tables) {
                System.out.printf("rows=%d, cols=%d\n", table.getRows().size(), table.getCols().size());

                for(List<RectangularTextContainer> rowCells : table.getRows()) {
                    for(RectangularTextContainer rowCell : rowCells) {
                        System.out.printf("%-20s", rowCell.getText());
                    }
                    System.out.println();
                }
            }
        }
    }
}
