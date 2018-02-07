package me.loki2302;

public class CalculatorApiImpl implements CalculatorApi {
    @Override
    public int addNumbers(int a, int b) {
        return a + b;
    }
}