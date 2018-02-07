package me.loki2302;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AppTest {
    @Test
    public void canSetProperties() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Person person = new Person();
        PropertyUtils.setProperty(person, "name", "loki2302");
        PropertyUtils.setProperty(person, "age", 123);
        PropertyUtils.setProperty(person, "alive", true);

        assertEquals("loki2302", person.getName());
        assertEquals(123, person.getAge());
        assertTrue(person.isAlive());
    }

    @Test
    public void canGetProperties() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Person person = new Person();
        person.setName("loki2302");
        person.setAge(123);
        person.setAlive(true);

        assertEquals("loki2302", PropertyUtils.getProperty(person, "name"));
        assertEquals(123, PropertyUtils.getProperty(person, "age"));
        assertEquals(true, PropertyUtils.getProperty(person, "alive"));
    }
}
