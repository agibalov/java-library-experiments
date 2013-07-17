package me.loki2302;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.exceptions.JadeException;
import de.neuland.jade4j.template.JadeTemplate;

public class JadeTest {
    @Test
    public void canRenderSimpleInlineTemplate() throws JadeException, IOException {
        JadeConfiguration configuration = new JadeConfiguration();
        configuration.setTemplateLoader(new TemplateNameIsTemplateItselfTemplateLoader());
        
        JadeTemplate jadeTemplate = configuration.getTemplate(
                "html\n" + 
                "  head\n" +
                "    title page title here\n" +
                "  body\n");
        Map<String, Object> model = new HashMap<String, Object>();
        String result = configuration.renderTemplate(jadeTemplate, model);
        assertEquals("<html><head><title>page title here</title></head><body></body></html>", result);
    }
}
