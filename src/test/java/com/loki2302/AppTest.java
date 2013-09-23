package com.loki2302;

import static com.mysema.query.collections.CollQueryFactory.from;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class AppTest {
    @Test
    public void canGetSingleItem() {
        List<Person> people = Arrays.asList(
                new Person(1, "loki2302"), 
                new Person(3, "jsmith"), 
                new Person(10, "john"));
        
        QPerson $ = QPerson.person;
        
        Person personById = from($, people).where($.id.eq(3)).singleResult($);
        assertEquals(people.get(1), personById);
        
        Person personByName = from($, people).where($.name.eq("loki2302")).singleResult($);
        assertEquals(people.get(0), personByName);
    }
}