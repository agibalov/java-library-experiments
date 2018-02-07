package me.loki2302;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.*;
import org.apache.poi.ss.formula.functions.FreeRefFunction;
import org.apache.poi.ss.formula.udf.DefaultUDFFinder;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class AppTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void canWriteAndReadAWorkbook() throws IOException, InvalidFormatException {
        File xlsxFile = temporaryFolder.newFile();

        if(true) {
            Workbook workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet();

            Row row = sheet.createRow(0);

            Cell cellA1 = row.createCell(0);
            cellA1.setCellValue("hello");

            Cell cellB1 = row.createCell(1);
            cellB1.setCellValue("world");

            try (OutputStream os = new FileOutputStream(xlsxFile)) {
                workbook.write(os);
            }
        }

        if(true) {
            Workbook workbook;
            try(InputStream is = new FileInputStream(xlsxFile)) {
                workbook = WorkbookFactory.create(is);
            }

            assertEquals(1, workbook.getNumberOfSheets());

            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            assertEquals("hello", row.getCell(0).getStringCellValue());
            assertEquals("world", row.getCell(1).getStringCellValue());
        }
    }

    @Test
    public void canComputeFormulas() {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet();

        Row row = sheet.createRow(0);

        Cell cellA1 = row.createCell(0);
        cellA1.setCellValue(2);

        Cell cellB1 = row.createCell(1);
        cellB1.setCellValue(3);

        Cell cellC1 = row.createCell(2);
        cellC1.setCellFormula("A1+B1");

        FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
        formulaEvaluator.evaluateAll();

        assertEquals("5", NumberToTextConverter.toText(sheet.getRow(0).getCell(2).getNumericCellValue()));
    }

    @Test
    public void canUseCustomFunctions() {
        Workbook workbook = new XSSFWorkbook();

        UDFFinder myUdfFinder = new DefaultUDFFinder(
                new String[] { "TWOTIMESSUM" },
                new FreeRefFunction[] { new TwoTimesSumFreeRefFunction() });
        workbook.addToolPack(myUdfFinder);

        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        Cell cellA1 = row.createCell(0);
        cellA1.setCellFormula("7 + TWOTIMESSUM(2, 3)");

        FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
        formulaEvaluator.evaluateAll();

        assertEquals("17", NumberToTextConverter.toText(sheet.getRow(0).getCell(0).getNumericCellValue()));
    }

    public static class TwoTimesSumFreeRefFunction implements FreeRefFunction {
        @Override
        public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
            if(args.length != 2) {
                return ErrorEval.VALUE_INVALID;
            }

            try {
                double a = OperandResolver.coerceValueToDouble(args[0]);
                double b = OperandResolver.coerceValueToDouble(args[1]);
                return new NumberEval(2 * (a + b));
            } catch (EvaluationException e) {
                e.printStackTrace();
                return e.getErrorEval();
            }
        }
    }
}
