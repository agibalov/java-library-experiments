package com.loki2302;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ch.lambdaj.function.convert.Converter;

import static ch.lambdaj.Lambda.*;
import static org.junit.Assert.*;

public class AppTest {
    @Test
    public void canExtractProperty() {
        List<Person> people = Arrays.asList(
                new Person(1, "loki2302"), 
                new Person(3, "jsmith"), 
                new Person(10, "john"));
        
        List<Integer> ids = extract(people, on(Person.class).getId());
        assertEquals(Arrays.asList(1, 3, 10), ids);
        
        List<String> names = extract(people, on(Person.class).getName());
        assertEquals(Arrays.asList("loki2302", "jsmith", "john"), names);
    }
    
    @Test
    public void canBuildMap() {
        List<Person> people = Arrays.asList(
                new Person(1, "loki2302"), 
                new Person(3, "jsmith"), 
                new Person(10, "john"));
        
        Map<Integer, Person> peopleByIds = index(people, on(Person.class).getId());
        assertEquals(3, peopleByIds.size());
        assertEquals(people.get(0), peopleByIds.get(1));
        assertEquals(people.get(1), peopleByIds.get(3));
        assertEquals(people.get(2), peopleByIds.get(10));
        
        Map<String, Person> peopleByNames = index(people, on(Person.class).getName());
        assertEquals(3, peopleByNames.size());
        assertEquals(people.get(0), peopleByNames.get("loki2302"));
        assertEquals(people.get(1), peopleByNames.get("jsmith"));
        assertEquals(people.get(2), peopleByNames.get("john"));
    }
    
    @Test
    public void canGetUnique() {
        List<Person> people = Arrays.asList(
                new Person(1, "loki2302"), 
                new Person(3, "jsmith"), 
                new Person(3, "jsmith2"),
                new Person(10, "john"));
        
        List<Integer> ids = extract(people, on(Person.class).getId());
        Collection<Integer> uniqueIds = selectDistinct(ids);
        assertEquals(3, uniqueIds.size());
        assertTrue(uniqueIds.contains(1));
        assertTrue(uniqueIds.contains(3));
        assertTrue(uniqueIds.contains(10));
    }
    
    @Test
    public void canConvert() {
        List<Person> people = Arrays.asList(
                new Person(1, "loki2302"), 
                new Person(3, "jsmith"), 
                new Person(10, "john"));
        
        List<CustomPerson> customPeople = convert(people, new Converter<Person, CustomPerson>() {
            @Override
            public CustomPerson convert(Person from) {
                CustomPerson customPerson = new CustomPerson(from.getId(), from.getName());
                return customPerson;
            }            
        });
        assertEquals(3, customPeople.size());
        assertEquals(1, customPeople.get(0).getId());
        assertEquals("loki2302", customPeople.get(0).getName());
        assertEquals(3, customPeople.get(1).getId());
        assertEquals("jsmith", customPeople.get(1).getName());
        assertEquals(10, customPeople.get(2).getId());
        assertEquals("john", customPeople.get(2).getName());
    }
    
    @Test
    public void canJoin() {
        List<Person> people = Arrays.asList(
                new Person(1, "loki2302"), 
                new Person(3, "jsmith"), 
                new Person(10, "john"));
        
        List<Article> articles = Arrays.asList(
                new Article(1, "article1", 3),
                new Article(2, "article2", 3),
                new Article(3, "article3", 1),
                new Article(4, "article4", 10));
        
        final Map<Integer, Person> peopleByIds = index(people, on(Person.class).getId());
        List<CompleteArticle> completeArticles = convert(articles, new Converter<Article, CompleteArticle>() {
            @Override
            public CompleteArticle convert(Article from) {
                CompleteArticle completeArticle = new CompleteArticle();
                completeArticle.setId(from.getId());
                completeArticle.setTitle(from.getTitle());
                
                int authorId = from.getAuthorId();
                Person author = peopleByIds.get(authorId);
                completeArticle.setAuthor(author);
                
                return completeArticle;
            }            
        });
        
        assertEquals(4, completeArticles.size());
        assertEquals(1, completeArticles.get(0).getId());
        assertEquals(people.get(1), completeArticles.get(0).getAuthor());
        assertEquals(2, completeArticles.get(1).getId());
        assertEquals(people.get(1), completeArticles.get(1).getAuthor());
        assertEquals(3, completeArticles.get(2).getId());
        assertEquals(people.get(0), completeArticles.get(2).getAuthor());
        assertEquals(4, completeArticles.get(3).getId());
        assertEquals(people.get(2), completeArticles.get(3).getAuthor());
    }
}
