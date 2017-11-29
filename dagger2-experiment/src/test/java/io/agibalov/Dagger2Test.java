package io.agibalov;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Dagger2Test {
    @Test
    public void itShouldWork() {
        CalculatorFactory calculatorFactory = DaggerCalculatorFactory
                .builder()
                .build();
        Calculator calculator = calculatorFactory.calculator();
        assertEquals(5, calculator.add(2, 3));
    }

    @Test
    public void canMockServices() {
        CalculatorFactory calculatorFactory = DaggerCalculatorFactory
                .builder()
                .calculatorModule(new CalculatorModule() {
                    @Override
                    public Adder adder() {
                        return new Adder() {
                            @Override
                            public int add(int a, int b) {
                                return 123;
                            }
                        };
                    }
                })
                .build();
        Calculator calculator = calculatorFactory.calculator();
        assertEquals(123, calculator.add(2, 3));
    }
}
