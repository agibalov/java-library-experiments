package io.agibalov;

import javax.inject.Inject;

public class Calculator {
    private final Adder adder;
    private final Subtractor subtractor;

    @Inject
    public Calculator(Adder adder, Subtractor subtractor) {
        this.adder = adder;
        this.subtractor = subtractor;
    }

    public int add(int a, int b) {
        return adder.add(a, b);
    }

    public int subtract(int a, int b) {
        return subtractor.subtract(a, b);
    }
}
