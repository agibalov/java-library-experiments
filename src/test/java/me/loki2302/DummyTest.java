package me.loki2302;

import org.junit.Test;
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DummyTest {
    @Test
    public void dummy() throws IOException {
        AmericanEnglish americanEnglish = new AmericanEnglish();
        JLanguageTool jLanguageTool = new JLanguageTool(americanEnglish);
        List<RuleMatch> matches = jLanguageTool.check("Orgonisation");
        assertEquals(1, matches.size());

        RuleMatch theOnlyMatch = matches.get(0);
        List<String> suggestedReplacements = theOnlyMatch.getSuggestedReplacements();
        assertEquals(suggestedReplacements, Arrays.asList("Organization"));
    }
}
