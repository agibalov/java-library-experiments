package me.loki2302.xml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlTest {
    @Test
    public void canSerializeCollectionAsItemItems() throws JsonProcessingException {
        PersonList personList = personList(
                person("loki2302"),
                person("Andrey")
        );

        XmlMapper xmlMapper = new XmlMapper();
        String xml = xmlMapper.writeValueAsString(personList);

        assertTrue(xml.contains("<people>"));
        assertTrue(xml.contains("</people>"));
        assertTrue(xml.contains("<person>"));
        assertTrue(xml.contains("</person>"));
    }

    @Test
    public void canDeserializeCollectionOfItemItems() throws IOException {
        String xml =
                "<PersonList>" +
                    "<people>" +
                        "<person><name>loki2302</name></person>" +
                        "<person><name>Andrey</name></person>" +
                    "</people>" +
                "</PersonList>";

        XmlMapper xmlMapper = new XmlMapper();
        PersonList personList = xmlMapper.readValue(xml, PersonList.class);
        assertEquals(2, personList.people.size());
        assertEquals("loki2302", personList.people.get(0).name);
        assertEquals("Andrey", personList.people.get(1).name);
    }

    private static PersonList personList(Person... people) {
        PersonList personList = new PersonList();
        personList.people = Arrays.asList(people);
        return personList;
    }

    private static Person person(String name) {
        Person person = new Person();
        person.name = name;
        return person;
    }

    public static class PersonList {
        @JacksonXmlElementWrapper(localName = "people")
        @JacksonXmlProperty(localName = "person")
        public List<Person> people;
    }

    public static class Person {
        public String name;
    }
}
