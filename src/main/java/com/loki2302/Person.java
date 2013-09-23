package com.loki2302;

import com.mysema.query.annotations.QueryEntity;

@QueryEntity
public class Person {
    private int id;
    private String name;
    
    public Person() {        
    }
    
    public Person(int id, String name) {
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
        return String.format("Person{id=%d,name=%s}", id, name);
    }
}