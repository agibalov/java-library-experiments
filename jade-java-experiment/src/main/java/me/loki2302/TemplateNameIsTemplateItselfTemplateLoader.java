package me.loki2302;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import de.neuland.jade4j.template.TemplateLoader;

public class TemplateNameIsTemplateItselfTemplateLoader implements TemplateLoader {
    @Override
    public long getLastModified(String arg0) throws IOException {
        return 0;
    }

    @Override
    public Reader getReader(String arg0) throws IOException {
        return new StringReader(arg0);
    }        
}