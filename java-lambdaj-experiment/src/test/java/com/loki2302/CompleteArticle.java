package com.loki2302;

public class CompleteArticle {
    private int id;
    private String title;
    private Person author;
    
    public CompleteArticle() {        
    }
    
    public CompleteArticle(int id, String title, Person author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Person getAuthor() {
        return author;
    }
    
    public void setAuthor(Person author) {
        this.author = author;
    }
    
    @Override
    public String toString() {
        return String.format("CompleteArticle{id=%d,title=%s,author=%s}", id, title, author);
    }
}