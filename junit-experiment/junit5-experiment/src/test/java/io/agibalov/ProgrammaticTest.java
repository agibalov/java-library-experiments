package io.agibalov;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.opentest4j.AssertionFailedError;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class ProgrammaticTest {
    @Test
    public void canDiscoverTests() {
        Launcher launcher = LauncherFactory.create();
        TestPlan testPlan = launcher.discover(LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(MyTest.class))
                .build());

        Map<String, TestIdentifier> testIdentifiersByDisplayNames = new HashMap<>();

        Stack<TestIdentifier> testIdentifiers = new Stack<>();
        testIdentifiers.addAll(testPlan.getRoots());
        while(!testIdentifiers.isEmpty()) {
            TestIdentifier testIdentifier = testIdentifiers.pop();
            testIdentifiersByDisplayNames.put(testIdentifier.getDisplayName(), testIdentifier);
            testPlan.getChildren(testIdentifier).forEach(testIdentifiers::push);
        }

        testIdentifiersByDisplayNames.values().forEach(testIdentifier ->
                System.out.printf("id=%s display=%s (isContainer=%b, isTest=%b)\n",
                        testIdentifier.getUniqueId(),
                        testIdentifier.getDisplayName(),
                        testIdentifier.isContainer(),
                        testIdentifier.isTest()));

        TestIdentifier myTestTestClass = testIdentifiersByDisplayNames.get("my cool tests");
        assertTrue(myTestTestClass.isContainer());
        assertFalse(myTestTestClass.isTest());

        TestIdentifier myTestOneTestMethod = testIdentifiersByDisplayNames.get("my test one");
        assertFalse(myTestOneTestMethod.isContainer());
        assertTrue(myTestOneTestMethod.isTest());

        TestIdentifier testTwoTestMethod = testIdentifiersByDisplayNames.get("testTwo()");
        assertFalse(testTwoTestMethod.isContainer());
        assertTrue(testTwoTestMethod.isTest());
    }

    @Test
    public void canRunTests() {
        SummaryGeneratingListener testExecutionListener = new SummaryGeneratingListener();

        Launcher launcher = LauncherFactory.create();
        launcher.execute(LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(MyTest.class))
                .build(), testExecutionListener);

        TestExecutionSummary testExecutionSummary = testExecutionListener.getSummary();
        assertEquals(2, testExecutionSummary.getTestsStartedCount());
        assertEquals(1, testExecutionSummary.getTestsSucceededCount());
        assertEquals(1, testExecutionSummary.getTestsFailedCount());

        TestExecutionSummary.Failure theOnlyFailure = testExecutionSummary.getFailures().get(0);
        assertEquals(AssertionFailedError.class, theOnlyFailure.getException().getClass());
        assertEquals("too bad", theOnlyFailure.getException().getMessage());
    }

    @DisplayName("my cool tests")
    @Tag("ignore-me")
    public static class MyTest {
        @Test
        @DisplayName("my test one")
        public void testOne() {
        }

        @Test
        public void testTwo() {
            fail("too bad");
        }
    }
}
