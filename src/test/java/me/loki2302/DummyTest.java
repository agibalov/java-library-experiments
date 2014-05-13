package me.loki2302;

import org.junit.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class DummyTest {
    @Test
    public void dummy() {
        Map<String, String> variables = new HashMap<String, String>();
        variables.put("message", "hello");
        String xml = render("1", variables);
        assertTrue(xml.contains("<name>hello</name>"));
    }

    private String render(String templateName, Map<String, ?> variables) {
        TemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode("XML");
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".xml");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariables(variables);

        String result = templateEngine.process(templateName, context);

        return result;
    }
}
