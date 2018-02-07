package me.loki2302;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class DummyTest {
    @Test
    public void dummy() {
        Person personA = person("1", "loki2302");
        Person personB = person("1", "vasya");

        Javers javers = JaversBuilder.javers().build();
        Diff diff = javers.compare(personA, personB);

        List<ValueChange> changes = diff.getChangesByType(ValueChange.class);
        assertEquals(1, changes.size());

        ValueChange change = changes.get(0);
        assertEquals("name", change.getPropertyName());
        assertEquals("loki2302", change.getLeft());
        assertEquals("vasya", change.getRight());
    }

    private static Person person(String id, String name) {
        Person person = new Person();
        person.id = id;
        person.name = name;
        return person;
    }

    public static class Person {
        public String id;
        public String name;
    }
}
