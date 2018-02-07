package me.loki2302;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;


import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class AppTest {
    private Serializer serializer = new Persister();
    
    @Test
    public void canSerialize() {
        Person p = new Person();
        p.id = 123;
        p.name = "loki2302";
        
        String xml = toXml(p);
        assertEquals(
                "<ThePerson TheName=\"loki2302\">\n" + 
                "   <TheId>123</TheId>\n" + 
                "</ThePerson>", xml);
    }
    
    @Test
    public void canDeserialize() {
        String xml =
                "<ThePerson TheName=\"loki2302\">\n" + 
                "   <TheId>123</TheId>\n" + 
                "</ThePerson>";
        
        Person p = fromXml(xml, Person.class);
        assertEquals(123, p.id);
        assertEquals("loki2302", p.name);
    }
    
    public String toXml(Object o) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            serializer.write(o, baos);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        byte[] bytes = baos.toByteArray();
        String xml = new String(bytes, Charset.forName("UTF-8"));
        
        return xml;
    }
    
    public <T> T fromXml(String xml, Class<T> clazz) {
        try {
            return serializer.read(clazz, xml);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}