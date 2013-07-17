package me.loki2302;

import java.util.Map;

import de.neuland.jade4j.filter.Filter;

public class MyDecoratingFilter implements Filter {
    @Override
    public String convert(String source, Map<String, Object> attributes, Map<String, Object> model) {
        return String.format("|%s|", source);
    }        
}