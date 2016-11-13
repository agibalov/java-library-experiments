package me.loki2302;

import lombok.Getter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) throws JRException {
        List<Item> items = Arrays.asList(
                new Item("Item One", 11),
                new Item("Item Two", 22));

        JasperReport jasperReport = JasperCompileManager.compileReport("test.jrxml");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("What", "World");

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(items);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrDataSource);

        JasperExportManager.exportReportToPdfFile(jasperPrint, "test.pdf");

        JasperViewer.viewReport(jasperPrint, true);
    }

    @Getter
    public static class Item {
        public String name;
        public int quantity;

        public Item(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }
    }
}
