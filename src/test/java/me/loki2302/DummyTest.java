package me.loki2302;

import org.junit.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class DummyTest {
    @Test
    public void canInsertText() {
        Map<String, String> variables = new HashMap<String, String>();
        variables.put("message", "hello");
        String xml = render("simple", variables);
        assertTrue(xml.contains("<name>hello</name>"));
    }

    @Test
    public void canInsertCollection() {
        List<Person> people = Arrays.asList(
                new Person(111, "john"),
                new Person(222, "jane")
        );

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("people", people);
        String xml = render("people", variables);
        assertTrue(xml.contains("<person id=\"111\">john</person>"));
        assertTrue(xml.contains("<person id=\"222\">jane</person>"));
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

    public static class Person {
        public int id;
        public String name;

        public Person(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
