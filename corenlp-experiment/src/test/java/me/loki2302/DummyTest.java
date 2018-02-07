package me.loki2302;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.Quadruple;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DummyTest {
    @Test
    public void canGetSentences() {
        Document document = new Document("Hello! My name is John and I work for Google. I drink vodka. Do you like vodka?");

        assertEquals(4, document.sentences().size());
        assertEquals("Hello!", document.sentence(0).text());
        assertEquals("My name is John and I work for Google.", document.sentence(1).text());
        assertEquals("I drink vodka.", document.sentence(2).text());
        assertEquals("Do you like vodka?", document.sentence(3).text());
    }

    @Test
    public void canGetSentenceLemmas() {
        Sentence sentence = new Sentence("My name is John");
        List<String> lemmas = sentence.lemmas();
        assertEquals(4, lemmas.size());
        assertEquals("my", lemmas.get(0));
        assertEquals("name", lemmas.get(1));
        assertEquals("be", lemmas.get(2));
        assertEquals("John", lemmas.get(3));
    }

    @Test
    public void canGetSentenceNamedEntities() {
        Sentence sentence = new Sentence("His name is John and he works for Google.");

        List<String> tags = sentence.nerTags();
        assertTrue(tags.contains("PERSON"));
        assertTrue(tags.contains("ORGANIZATION"));

        assertTrue(sentence.mentions("PERSON").contains("John"));
        assertTrue(sentence.mentions("ORGANIZATION").contains("Google"));
    }

    @Test
    public void canGetOpenIEs() {
        Sentence sentence = new Sentence("His name is John and he works for Google.");
        Collection<Quadruple<String, String, String, Double>> openies = sentence.openie();

        assertEquals(2, openies.size());

        assertTrue(openies.stream().anyMatch(q ->
                q.first().equals("he") &&
                        q.second().equals("works for") &&
                        q.third().equals("Google")));

        assertTrue(openies.stream().anyMatch(q ->
                q.first().equals("His name") &&
                        q.second().equals("is") &&
                        q.third().equals("John")));
    }

    @Test
    public void canGetPOSTags() {
        Sentence sentence = new Sentence("Cats don't eat apples");
        List<String> tags = sentence.posTags();

        // http://www.ling.upenn.edu/courses/Fall_2003/ling001/penn_treebank_pos.html
        assertEquals(5, tags.size());
        assertEquals("NNS", tags.get(0)); // Noun, plural
        assertEquals("VBP", tags.get(1)); // Verb, non-3rd person singular present
        assertEquals("RB", tags.get(2)); // Adverb
        assertEquals("VB", tags.get(3)); // Verb, base form
        assertEquals("NNS", tags.get(4)); // Noun, plural
    }
}
