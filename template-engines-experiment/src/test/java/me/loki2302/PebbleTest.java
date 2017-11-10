package me.loki2302;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.lexer.Syntax;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PebbleTest {
    @Test
    public void dummy() throws PebbleException, IOException {
        PebbleEngine pebbleEngine = new PebbleEngine.Builder()
                .loader(new StringLoader())
                .autoEscaping(true)
                .defaultEscapingStrategy("js")
                .build();
        PebbleTemplate template = pebbleEngine.getTemplate("something: {{username}}");
        Map<String, Object> model = new HashMap<>();
        model.put("username", "{{xxx}}\ndrop: table");

        StringWriter stringWriter = new StringWriter();
        template.evaluate(stringWriter, model);
        String result = stringWriter.toString();
        assertEquals("something: {{xxx}}\\u000Adrop: table", result);
    }
}
