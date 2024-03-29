package com.loki2302;

public class CustomPerson {
    private int id;
    private String name;
    
    public CustomPerson() {        
    }
    
    public CustomPerson(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return String.format("CustomPerson{id=%d,name=%s}", id, name);
    }
}