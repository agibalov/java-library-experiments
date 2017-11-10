package me.loki2302;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.rythmengine.Rythm;

public class RythmTest {
    @Test
    public void canRenderSimpleInlineTemplate() {
        String s = Rythm.render("Hello, @name!", "loki2302");
        assertEquals("Hello, loki2302!", s);
    }
    
    @Test
    public void canRenderObject() {
        Person person = new Person();
        person.id = 123;
        person.name = "loki2302";
        
        String s = Rythm.render(
                "@args me.loki2302.Person person\n" + 
                "Person has id @person.id and name @person.name", 
                person);
        assertEquals("Person has id 123 and name loki2302", s);
    }
    
    @Test
    public void canRenderList() {
        List<Integer> model = Arrays.asList(1, 2, 3);
        String s = Rythm.render(
                "@args List<Integer> items\n" + 
                "@for(int i : items) {@i}", model);
        assertEquals("123", s);
    }
    
    @Test
    public void canUseResourceTemplate() {
        Person person = new Person();
        person.id = 123;
        person.name = "loki2302";
        
        String s = Rythm.render("/greetings.html", person);
        assertEquals("Person has id 123 and name loki2302", s);        
    }
    
    @Test
    public void canUseSections() {
        Person person = new Person();
        person.id = 123;
        person.name = "loki2302";
        String s = Rythm.render("/layouted-greetings.html", person);
        assertEquals("Hello, loki2302!Your id is 123", s);        
    }
    
    @Test
    public void canUseTransformer() {
        Rythm.engine().registerTransformer("test", "", MyDecoratingTransformer.class);
        
        Person person = new Person();
        person.id = 123;
        person.name = "loki2302";
        String s = Rythm.render(
                "@args me.loki2302.Person person\n" +
                "Hello, @person.name.test_decorate()!", 
                person);
        assertEquals("Hello, |loki2302|!", s);
    }
}