package me.loki2302.customrunner;

import org.junit.runners.parameterized.TestWithParameters;

public class MyRunnerWithParameters extends MyRunner {
    public MyRunnerWithParameters(TestWithParameters test) {
        super(test.getTestClass().getJavaClass(), test.getParameters(), test.getName());
    }
}
