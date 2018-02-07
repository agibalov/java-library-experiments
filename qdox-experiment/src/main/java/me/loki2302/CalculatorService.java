package me.loki2302;


import java.util.function.Function;

/**
 * Implements calculator functionality
 */
public class CalculatorService {
    /**
     * @param a number A
     * @param b number B
     * @return sum of a and b
     */
    public int addNumbers(int a, int b) {
        return a + b;
    }

    public int someJava8Stuff() {
        Function<Integer, Integer> f = a -> a + 1;
        return f.apply(2);
    }
}
