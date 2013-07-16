package me.loki2302;

import org.rythmengine.Rythm;

public class App {
    public static void main(String[] args) {
        String s = Rythm.render("Hello, @name!", "loki2302");
        System.out.printf("'%s'\n", s);
    }
}
