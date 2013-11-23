package me.loki2302;

import static org.junit.Assert.*;

import org.junit.Test;

public class AppTest {
    @Test
    public void dummy() {
        Api api = new Api();
        try {
            api.add(2, 3);
        } catch(CallDetailsException e) {
            assertEquals("add", e.getMethodName());
            assertEquals(2, e.getArgs().length);
            assertEquals(2, e.getArgs()[0]);
            assertEquals(3, e.getArgs()[1]);
            
            return;
        }
        
        fail();
    }
}