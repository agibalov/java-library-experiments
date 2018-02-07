package me.loki2302;

import org.docx4j.convert.in.xhtml.XHTMLImporter;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.File;

public class App {
    public static void main(String[] args) throws Docx4JException {

        String html = String.join("",
                "<div style='width:100%'>",
                "  <p>hello</p>",
                "  <p style='color:red'>there</p>",
                "  <table style='width:200pt;' border='1'>",
                "    <tbody>",
                "      <tr>",
                "        <td style='background-color:#eeffff'>cell1</td>",
                "        <td colspan='2'>cell2</td>",
                "      </tr>",
                "      <tr>",
                "        <td>cell4</td>",
                "        <td>cell5</td>",
                "        <td>cell6</td>",
                "      </tr>",
                "    </tbody>",
                "  </table>",
                "</div>");

        WordprocessingMLPackage wordprocessingMLPackage = WordprocessingMLPackage.createPackage();
        XHTMLImporter xhtmlImporter = new XHTMLImporterImpl(wordprocessingMLPackage);
        wordprocessingMLPackage.getMainDocumentPart().getContent().addAll(xhtmlImporter.convert(html, null));
        wordprocessingMLPackage.save(new File("1.docx"));
    }
}
