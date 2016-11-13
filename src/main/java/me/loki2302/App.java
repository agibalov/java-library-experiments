package me.loki2302;

import net.sf.jasperreports.engine.*;

import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main(String[] args) throws JRException {
        JasperReport jasperReport = JasperCompileManager.compileReport("test.jrxml");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("What", "World");

        JRDataSource jrDataSource = new JREmptyDataSource();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrDataSource);

        JasperExportManager.exportReportToPdfFile(jasperPrint, "test.pdf");
    }
}
