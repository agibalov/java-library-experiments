package me.loki2302;

import edu.stanford.nlp.simple.Document;

public class App {
    public static void main(String[] args) {
        Document document = new Document("Hello! My name is John and I work for Google. I drink vodka. Do you like vodka?");
        System.out.printf("sentences: %d\n", document.sentences().size());
        System.out.printf("lemmas: %s\n", document.sentence(1).lemmas());
        System.out.printf("NER tags: %s\n", document.sentence(1).mentions("PERSON"));
        System.out.printf("NER tags: %s\n", document.sentence(1).mentions("ORGANIZATION"));
        System.out.printf("NER tags: %s\n", document.sentence(2).nerTags());
    }
}
