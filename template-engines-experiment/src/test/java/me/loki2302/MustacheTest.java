package me.loki2302;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class MustacheTest {
    @Test
    public void dummy() {
        MustacheFactory mustacheFactory = new DefaultMustacheFactory();
        Mustache template = mustacheFactory.compile(new StringReader("{{something}}"), "template");
        String result = template.execute(new StringWriter(), new HashMap<String, Object>() {{
            put("something", "omg\nwtf\nbbq");
        }}).toString();
        assertEquals("omg&#10;wtf&#10;bbq", result);
    }
}
