package me.loki2302;

import org.rythmengine.extension.Transformer;

public class MyDecoratingTransformer {
    @Transformer
    public static String decorate(String s) {
        return String.format("|%s|", s);
    }
}