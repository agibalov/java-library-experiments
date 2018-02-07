package com.loki2302;

public class Article {
    private int id;
    private String title;
    private int authorId;
    
    public Article() {        
    }
    
    public Article(int id, String title, int authorId) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
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
    
    public int getAuthorId() {
        return authorId;
    }
    
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }
    
    @Override
    public String toString() {
        return String.format("Article{id=%d,title=%s,authorId=%d}", id, title, authorId);
    }
}