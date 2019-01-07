package me.loki2302;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.extractProperty;

public class FestTest {
    @Test
    public void canTestIfOneIsOne() {
        assertThat(1).isEqualTo(1);
    }

    @Test
    public void canTestIntArray() {
        assertThat(new int[]{1, 2, 3})
                .isNotEmpty()
                .hasSize(3)
                .contains(1, 2)
                .containsOnly(2, 3, 1)
                .containsSequence(2, 3)
                .doesNotContain(4, 5)
                .endsWith(2, 3)
                .startsWith(1, 2)
                .isSorted();

        // TODO: how do I check if every single item is OK? :-/
    }

    @Test
    public void canTestPersonCollection() {
        List<Person> people = Arrays.asList(
                person(1, "loki2302"),
                person(2, "Andrey"),
                person(3, "john")
        );

        // FEST wants Integer.class, not int.class
        assertThat(extractProperty("id", Integer.class).from(people))
                .containsSequence(1, 2, 3);

        assertThat(extractProperty("name").ofType(String.class).from(people))
                .containsExactly("loki2302", "Andrey", "john");
    }

    private static Person person(int id, String name) {
        Person person = new Person();
        person.id = id;
        person.name = name;
        return person;
    }

    // FEST wants it public
    public static class Person {
        public int id;
        public String name;

        // FEST wants getters
        public int getId() {
            return id;
        }

        // FEST wants getters
        public String getName() {
            return name;
        }
    }
}
