package me.loki2302;

public class Calculator {
    public int addNumbers(int a, int b) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}
        
        if(a > 10 || b > 10) {
            throw new IllegalArgumentException("at least one of the numbers is too large");
        }
        
        return a + b;
    }
}