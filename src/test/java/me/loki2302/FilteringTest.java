package me.loki2302;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunListener;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FilteringTest {
    private final static String SPEL_FILTER_EXPRESSION_SYSTEM_PROPERTY_NAME = "MYFILTER";

    private JUnitCore jUnitCore;
    private RecordingRunListener recordingRunListener;

    @Before
    public void init() {
        jUnitCore = new JUnitCore();
        recordingRunListener = new RecordingRunListener();
        jUnitCore.addListener(recordingRunListener);
    }

    @Test
    public void canRunAllTests() {
        System.setProperty(SPEL_FILTER_EXPRESSION_SYSTEM_PROPERTY_NAME, "true");
        jUnitCore.run(DummyTest.class);
        assertEquals(4, recordingRunListener.testsRun.size());
    }

    @Test
    public void canRunSlowTests() {
        System.setProperty(SPEL_FILTER_EXPRESSION_SYSTEM_PROPERTY_NAME, "slow()");
        jUnitCore.run(DummyTest.class);
        assertEquals(2, recordingRunListener.testsRun.size());
        assertTrue(recordingRunListener.testsRun.contains("slowTrueWritesTrue"));
        assertTrue(recordingRunListener.testsRun.contains("slowTrueWritesFalse"));
    }

    @Test
    public void canRunWritingTests() {
        System.setProperty(SPEL_FILTER_EXPRESSION_SYSTEM_PROPERTY_NAME, "writes()");
        jUnitCore.run(DummyTest.class);
        assertEquals(2, recordingRunListener.testsRun.size());
        assertTrue(recordingRunListener.testsRun.contains("slowTrueWritesTrue"));
        assertTrue(recordingRunListener.testsRun.contains("slowFalseWritesTrue"));
    }

    @Test
    public void canRunNonSlowWritingTests() {
        System.setProperty(SPEL_FILTER_EXPRESSION_SYSTEM_PROPERTY_NAME, "!slow() && writes()");
        jUnitCore.run(DummyTest.class);
        assertEquals(1, recordingRunListener.testsRun.size());
        assertTrue(recordingRunListener.testsRun.contains("slowFalseWritesTrue"));
    }

    @RunWith(RunnerWithSpelFilter.class)
    public static class DummyTest {
        @Test
        @Tag(slow = true, writes = true)
        public void slowTrueWritesTrue() {}

        @Test
        @Tag(slow = true, writes = false)
        public void slowTrueWritesFalse() {}

        @Test
        @Tag(slow = false, writes = true)
        public void slowFalseWritesTrue() {}

        @Test
        @Tag(slow = false, writes = false)
        public void slowFalseWritesFalse() {}
    }

    public static class SpelFilter extends Filter {
        private final String expressionString;

        public SpelFilter(String expressionString) {
            this.expressionString = expressionString;
        }

        @Override
        public boolean shouldRun(Description description) {
            Tag tag = description.getAnnotation(Tag.class);

            StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
            evaluationContext.setRootObject(tag);

            ExpressionParser expressionParser = new SpelExpressionParser();
            Expression expression = expressionParser.parseExpression(expressionString);
            boolean shouldRun = (boolean)(Boolean)expression.getValue(evaluationContext);

            return shouldRun;
        }

        @Override
        public String describe() {
            return "My filter description here";
        }
    }

    public static class RunnerWithSpelFilter extends BlockJUnit4ClassRunner {
        public RunnerWithSpelFilter(Class<?> klass) throws InitializationError {
            super(klass);
            String expressionString = System.getProperty(SPEL_FILTER_EXPRESSION_SYSTEM_PROPERTY_NAME);
            SpelFilter spelFilter = new SpelFilter(expressionString);
            try {
                spelFilter.apply(this);
            } catch (NoTestsRemainException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class RecordingRunListener extends RunListener {
        public Set<String> testsRun = new HashSet<>();

        @Override
        public void testStarted(Description description) throws Exception {
            String testMethodName = description.getMethodName();
            testsRun.add(testMethodName);
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Tag {
        boolean slow();
        boolean writes();
    }
}
