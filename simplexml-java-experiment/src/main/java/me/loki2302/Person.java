package me.loki2302;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "ThePerson")
public class Person {
    @Element(name = "TheId")
    public int id;
    
    @Attribute(name = "TheName")
    public String name;
    
    @Override
    public String toString() {
        return String.format("Person{id=%d, name='%s'}", id, name);
    }
}