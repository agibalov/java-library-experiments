package me.loki2302.customrunner;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class MyRunner extends Runner {
    private final Class<?> targetClass;
    private final List<Object> parameters;
    private final String testName;

    public MyRunner(Class<?> targetClass) {
        this(targetClass, null, null);
    }

    public MyRunner(Class<?> targetClass, List<Object> parameters, String testName) {
        this.targetClass = targetClass;
        this.parameters = parameters;
        this.testName = testName;
    }

    @Override
    public Description getDescription() {
        return Description.createSuiteDescription(targetClass);
    }

    @Override
    public void run(RunNotifier notifier) {
        TestClass testClass = new TestClass(targetClass);
        Object testObject;
        Constructor<?> constructor = testClass.getOnlyConstructor();
        try {
            if(parameters == null) {
                testObject = constructor.newInstance();
            } else {
                Object[] parametersArray = parameters.toArray(new Object[parameters.size()]);
                testObject = constructor.newInstance(parametersArray);
            }
        } catch (InvocationTargetException e) {
            throw new RuntimeException();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        List<FrameworkMethod> testMethods = testClass.getAnnotatedMethods(MyTest.class);
        for (FrameworkMethod testMethod : testMethods) {
            String name = testName == null ? testMethod.getName() : testName;
            Description testDescription = Description.createTestDescription(targetClass, name);

            MyIgnore myIgnore = testMethod.getAnnotation(MyIgnore.class);
            if(myIgnore != null) {
                notifier.fireTestIgnored(testDescription);
                continue;
            }

            notifier.fireTestStarted(testDescription);
            try {
                testMethod.invokeExplosively(testObject);
            } catch (Throwable throwable) {
                notifier.fireTestFailure(new Failure(testDescription, throwable));
            } finally {
                notifier.fireTestFinished(testDescription);
            }
        }
    }
}
