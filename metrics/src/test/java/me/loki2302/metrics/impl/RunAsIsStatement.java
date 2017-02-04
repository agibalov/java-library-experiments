package me.loki2302.metrics.impl;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.util.List;

public class RunAsIsStatement extends Statement {
    private final String className;
    private final String methodName;
    private final Object target;
    private final List<FrameworkMethod> beforeIterationMethods;
    private final List<FrameworkMethod> afterIterationMethods;
    private final Statement iterationStatement;

    public RunAsIsStatement(
            String className,
            String methodName,
            Object target,
            List<FrameworkMethod> beforeIterationMethods,
            List<FrameworkMethod> afterIterationMethods,
            Statement iterationStatement) {

        this.className = className;
        this.methodName = methodName;
        this.target = target;
        this.beforeIterationMethods = beforeIterationMethods;
        this.afterIterationMethods = afterIterationMethods;
        this.iterationStatement = iterationStatement;
    }

    @Override
    public void evaluate() throws Throwable {
        runBeforeIterationMethods();
        try {
            iterationStatement.evaluate();
        } finally {
            runAfterIterationMethods();
        }
    }

    private void runBeforeIterationMethods() throws Throwable {
        for(FrameworkMethod method : beforeIterationMethods) {
            method.invokeExplosively(target);
        }
    }

    private void runAfterIterationMethods() throws Throwable {
        for(FrameworkMethod method : afterIterationMethods) {
            method.invokeExplosively(target);
        }
    }
}
