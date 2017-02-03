package me.loki2302.metrics.impl;

import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
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

        statement = withBeforeIterationMethods(testClass, statement, test);
        statement = withAfterIterationMethods(testClass, statement, test);

        if(!System.getProperty(ENABLE_BENCHMARK_PROPERTY_NAME, "").equals("")) {
            statement = withBenchmark(statement, method);
        }

        return statement;
    }

    private static Statement withBeforeIterationMethods(TestClass testClass, Statement statement, Object test) {
        List<FrameworkMethod> beforeFrameworkMethods = testClass.getAnnotatedMethods(BeforeIteration.class);
        return beforeFrameworkMethods.isEmpty() ? statement : new RunBefores(statement, beforeFrameworkMethods, test);
    }

    private static Statement withAfterIterationMethods(TestClass testClass, Statement statement, Object test) {
        List<FrameworkMethod> afterFrameworkMethods = testClass.getAnnotatedMethods(AfterIteration.class);
        return afterFrameworkMethods.isEmpty() ? statement : new RunAfters(statement, afterFrameworkMethods, test);
    }

    private static Statement withBenchmark(
            Statement statement,
            FrameworkMethod method) {

        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();

        Benchmark benchmark = method.getAnnotation(Benchmark.class);
        return benchmark == null ? statement : new RunBenchmarkStatement(className, methodName, statement, benchmark);
    }
}
