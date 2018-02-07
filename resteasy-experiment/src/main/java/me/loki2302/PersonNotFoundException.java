package me.loki2302;

public class PersonNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    private final int id;
    
    public PersonNotFoundException(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
}