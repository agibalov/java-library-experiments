package com.loki2302;

import static com.mysema.query.collections.CollQueryFactory.from;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;




public class AppTest {
    @Test
    public void canExtractProperty() {
        List<Person> people = Arrays.asList(
                new Person(1, "loki2302"), 
                new Person(3, "jsmith"), 
                new Person(10, "john"));
        
        QPerson $ = QPerson.person;
        
        List<Integer> ids = from($, people).list($.id);
        assertEquals(Arrays.asList(1, 3, 10), ids);
        
        List<String> names = from($, people).list($.name);
        assertEquals(Arrays.asList("loki2302", "jsmith", "john"), names);
    }
    
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
    
    @Test
    public void canFilterItems() {
        List<Person> people = Arrays.asList(
                new Person(1, "loki2302"), 
                new Person(3, "jsmith"), 
                new Person(10, "john"));
        
        QPerson $ = QPerson.person;
        
        List<Person> filteredPersons = from($, people).where($.name.contains("i")).list($);
        assertEquals(2, filteredPersons.size());
        assertEquals(people.get(0), filteredPersons.get(0));
        assertEquals(people.get(1), filteredPersons.get(1));
    }
    
    @Test
    public void canConvert() {
        List<Person> people = Arrays.asList(
                new Person(1, "loki2302"), 
                new Person(3, "jsmith"), 
                new Person(10, "john"));
        
        QPerson $ = QPerson.person;
        
        List<CustomPerson> customPeople = from($, people).list(QCustomPerson.create($.id, $.name)); 
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
        
        QArticle article = QArticle.article;
        QPerson person = QPerson.person;
        List<CompleteArticle> completeArticles = 
                from(article, articles)
                .from(person, people)
                .where(article.authorId.eq(person.id))
                .list(QCompleteArticle.create(article.id, article.title, person));
        
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