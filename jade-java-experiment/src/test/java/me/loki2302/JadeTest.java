package me.loki2302;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.neuland.jade4j.Jade4J.Mode;
import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.exceptions.JadeException;
import de.neuland.jade4j.filter.MarkdownFilter;
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
    
    @Test
    public void canUseEmbeddedMarkdown() throws JadeException, IOException {
        JadeConfiguration configuration = new JadeConfiguration();
        configuration.setTemplateLoader(new TemplateNameIsTemplateItselfTemplateLoader());
        configuration.setMode(Mode.XML);
        configuration.setFilter("markdown", new MarkdownFilter());        
                
        JadeTemplate jadeTemplate = configuration.getTemplate(
                "obj\n" + 
                "  :markdown\n" +
                "    hi *there* <>");
        Map<String, Object> model = new HashMap<String, Object>();
        String result = configuration.renderTemplate(jadeTemplate, model);
        assertEquals("<obj><p>hi <em>there</em> &lt;&gt;</p></obj>", result);
    }
    
    @Test
    public void canUseCustomFilter() throws JadeException, IOException {
        JadeConfiguration configuration = new JadeConfiguration();
        configuration.setTemplateLoader(new TemplateNameIsTemplateItselfTemplateLoader());
        configuration.setMode(Mode.XML);
        configuration.setFilter("decorate", new MyDecoratingFilter());        
                
        JadeTemplate jadeTemplate = configuration.getTemplate(
                "obj\n" + 
                "  :decorate\n" +
                "    loki2302");
        Map<String, Object> model = new HashMap<String, Object>();
        String result = configuration.renderTemplate(jadeTemplate, model);
        assertEquals("<obj>|loki2302|</obj>", result);
    }
}
