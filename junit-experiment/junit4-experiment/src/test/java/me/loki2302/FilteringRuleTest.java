package me.loki2302;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runners.model.Statement;
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

/**
 * Test filtering using JUnit TestRule. Same as {@link FilteringTest}, but doesn't
 * require a custom runner. Pitfalls are: it relies on assumptions mechanism,
 * so reporting may be inaccurate.
 */
public class FilteringRuleTest {
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

    public static class DummyTest {
        @Rule
        public final FilteringRule filteringRule = new FilteringRule();

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

    public static class FilteringRule implements TestRule {
        @Override
        public Statement apply(Statement base, Description description) {
            String expressionString = System.getProperty(SPEL_FILTER_EXPRESSION_SYSTEM_PROPERTY_NAME);

            Tag tag = description.getAnnotation(Tag.class);
            StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
            evaluationContext.setRootObject(tag);

            ExpressionParser expressionParser = new SpelExpressionParser();
            Expression expression = expressionParser.parseExpression(expressionString);
            boolean shouldRun = (boolean)(Boolean)expression.getValue(evaluationContext);

            if(!shouldRun) {
                return new IgnoringStatement(expressionString);
            }

            return base;
        }

        private static class IgnoringStatement extends Statement {
            private final String expressionString;

            private IgnoringStatement(String expressionString) {
                this.expressionString = expressionString;
            }

            @Override
            public void evaluate() throws Throwable {
                Assume.assumeTrue("Ignoring! " + expressionString, false);
            }
        }
    }

    private static class RecordingRunListener extends RunListener {
        public final Set<String> testsRun = new HashSet<>();

        @Override
        public void testStarted(Description description) throws Exception {
            String testMethodName = description.getMethodName();
            testsRun.add(testMethodName);
        }

        @Override
        public void testAssumptionFailure(Failure failure) {
            String testMethodName = failure.getDescription().getMethodName();
            testsRun.remove(testMethodName);
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Tag {
        boolean slow();
        boolean writes();
    }
}
