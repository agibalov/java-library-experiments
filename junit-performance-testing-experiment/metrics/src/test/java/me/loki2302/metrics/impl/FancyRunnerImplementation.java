package me.loki2302.metrics.impl;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.util.List;

public class FancyRunnerImplementation {
    private final static String ENABLE_BENCHMARK_PROPERTY_NAME = "ENABLE_BENCHMARK";

    public Statement decorateStatement(
            TestClass testClass,
            Statement statement,
            FrameworkMethod method,
            Object test) {

        List<FrameworkMethod> beforeIterationMethods = testClass.getAnnotatedMethods(BeforeIteration.class);
        List<FrameworkMethod> afterIterationMethods = testClass.getAnnotatedMethods(AfterIteration.class);

        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();

        boolean isBenchmarkModeEnabled = !System.getProperty(ENABLE_BENCHMARK_PROPERTY_NAME, "").equals("");
        Benchmark benchmark = method.getAnnotation(Benchmark.class);
        boolean shouldEvaluateAsBenchmark = isBenchmarkModeEnabled && benchmark != null;
        if(shouldEvaluateAsBenchmark) {
            statement = new RunAsBenchmarkStatement(
                    className,
                    methodName,
                    test,
                    beforeIterationMethods,
                    afterIterationMethods,
                    statement,
                    benchmark);
        } else {
            statement = new RunAsIsStatement(
                    className,
                    methodName,
                    test,
                    beforeIterationMethods,
                    afterIterationMethods,
                    statement);
        }

        return statement;
    }
}
