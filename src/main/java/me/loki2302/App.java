package me.loki2302;

import lombok.Getter;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.chart.BarChartBuilder;
import net.sf.dynamicreports.report.builder.chart.PieChartBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.components.items.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class App {
    public static void main(String[] args) throws DRException {
        List<Item> items = new ArrayList<>();
        for(int i = 0; i < 10; ++i) {
            // the weird thing is, it won't actual do any grouping if I do i % 2
            items.add(new Item(String.format("Item #%d", i + 1), String.format("Category #%d", i < 5 ? 1 : 2), 1 + 2 * i));
        }

        StyleBuilder boldStyle = stl.style().bold();
        StyleBuilder columnTitleStyle = stl.style(boldStyle).setBorder(stl.pen1Point());

        TextColumnBuilder<String> itemColumn =
                col.column("Item", "name", type.stringType());
        TextColumnBuilder<Integer> quantityColumn =
                col.column("Quantity", "quantity", type.integerType());
        TextColumnBuilder<String> categoryColumn =
                col.column("Category", "category", type.stringType());

        BarChartBuilder myBarChart = cht.barChart()
                .setTitle("My Fancy Bar Chart")
                .setCategory(itemColumn)
                .addSerie(cht.serie(quantityColumn));

        PieChartBuilder myPieChart = cht.pieChart()
                .setTitle("My Fancy Pie Chart")
                .setKey(itemColumn)
                .addSerie(cht.serie(quantityColumn));

        report()
                .setColumnTitleStyle(columnTitleStyle)
                .setDetailStyle(stl.style(stl.pen1Point()))
                .setColumnStyle(stl.style(stl.pen1Point()))
                .highlightDetailEvenRows()
                .title(cmp.text("My Fancy Report"))
                .columns(
                        itemColumn,
                        quantityColumn)
                .subtotalsAtSummary(sbt.sum(quantityColumn))
                .groupBy(categoryColumn)
                .summary(myBarChart, myPieChart)
                .setDataSource(items)
                .toPdf(export.pdfExporter("1.pdf"))
                .show();
    }

    @Getter
    public static class Item {
        public String name;
        public String category;
        public int quantity;

        public Item(String name, String category, int quantity) {
            this.name = name;
            this.category = category;
            this.quantity = quantity;
        }
    }
}
