package com.loki2302;

import static com.mysema.query.collections.CollQueryFactory.*;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {
        List<Person> people = Arrays.asList(
                new Person(1, "loki2302"), 
                new Person(3, "jsmith"), 
                new Person(10, "john"));
        
        QPerson $ = QPerson.person;
        List<Person> filteredPeople = from($, people).where($.name.contains("i")).list($);
        System.out.println(filteredPeople.size());
    } 
}
