package io.agibalov;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

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

    // https://github.com/FasterXML/jackson-dataformat-xml/issues/124
    @Test
    public void canDeserializeEmptyCollection() throws IOException {
        String xml =
                "<PersonList>" +
                    "<people />" +
                "</PersonList>";

        XmlMapper xmlMapper = new XmlMapper();
        PersonList personList = xmlMapper.readValue(xml, PersonList.class);
        assertNull(personList.people);

        // Should be:
        // assertNotNull(personList.people);
        // assertEquals(0, personList.people.size());
    }

    @Test
    public void xmlMapper124Workaround() throws IOException {
        XmlMapper xmlMapper = new XmlMapper();

        PersonListWorkaround nullPersonList = xmlMapper.readValue(
                "<PersonList></PersonList>", PersonListWorkaround.class);
        assertNull(nullPersonList.people);

        PersonListWorkaround emptyPersonList = xmlMapper.readValue(
                "<PersonList><people /></PersonList>", PersonListWorkaround.class);
        assertNotNull(emptyPersonList.people);
        assertEquals(0, emptyPersonList.people.size());

        PersonListWorkaround notEmptyPersonList = xmlMapper.readValue(
                "<PersonList><people>" +
                        "<person><name>loki2302</name></person><person><name>Andrey</name></person>" +
                "</people></PersonList>", PersonListWorkaround.class);
        assertNotNull(notEmptyPersonList.people);
        assertEquals(2, notEmptyPersonList.people.size());
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

    public static class PersonListWorkaround {
        @JacksonXmlElementWrapper(localName = "people")
        @JacksonXmlProperty(localName = "person")
        public List<Person> people;

        public void setPeople(List<Person> people) {
            if(people == null) {
                people = new ArrayList<Person>();
            }

            this.people = people;
        }
    }

    public static class Person {
        public String name;
    }
}
