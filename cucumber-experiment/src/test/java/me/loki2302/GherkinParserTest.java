package me.loki2302;

import gherkin.AstBuilder;
import gherkin.Parser;
import gherkin.ast.GherkinDocument;
import gherkin.ast.Scenario;
import gherkin.ast.Tag;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class GherkinParserTest {
    @Test
    public void canParseGherkin() {
        //language=Gherkin
        String gherkin = "# qwerty\n" +
                "Feature: dummy\n" +
                "  It should work.\n" +
                "\n" +
                "  # hey there!\n" +
                "  @omg\n" +
                "  @wtf\n" +
                "  Scenario: Eat apples and see what happens\n" +
                "    Given I have 10 apples\n" +
                "    When I eat 3 apples\n" +
                "    Then I still have 7 apples\n" +
                "\n" +
                "  Scenario: Eat more apples and see what happens\n" +
                "    Given I have 10 apples\n" +
                "    When I eat 5 apples\n" +
                "    Then I still have 3 apples\n";

        Parser<GherkinDocument> parser = new Parser<>(new AstBuilder());
        GherkinDocument gherkinDocument = parser.parse(gherkin);

        assertEquals(2, gherkinDocument.getComments().size());
        assertEquals("# qwerty", gherkinDocument.getComments().get(0).getText());
        assertEquals("  # hey there!", gherkinDocument.getComments().get(1).getText());
        assertEquals(5, gherkinDocument.getComments().get(1).getLocation().getLine());
        assertEquals(1, gherkinDocument.getComments().get(1).getLocation().getColumn());

        assertEquals("dummy", gherkinDocument.getFeature().getName());
        assertEquals(2, gherkinDocument.getFeature().getChildren().size());
        assertEquals("Eat apples and see what happens", gherkinDocument.getFeature().getChildren().get(0).getName());

        List<Tag> tags = ((Scenario)gherkinDocument.getFeature().getChildren().get(0)).getTags();
        assertEquals(2, tags.size());
        assertEquals("@omg", tags.get(0).getName());
        assertEquals("@wtf", tags.get(1).getName());
    }
}
