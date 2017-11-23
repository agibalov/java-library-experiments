package me.loki2302.shared;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.util.ArrayList;
import java.util.List;

public class RecordingRunListener extends RunListener {
    public final List<String> events = new ArrayList<>();

    @Override
    public void testRunStarted(Description description) throws Exception {
        events.add(String.format("testRunStarted %s", description.getDisplayName()));
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        events.add(String.format("testRunFinished %b", result.wasSuccessful()));
    }

    @Override
    public void testStarted(Description description) throws Exception {
        events.add(String.format("testStarted %s", description.getDisplayName()));
    }

    @Override
    public void testFinished(Description description) throws Exception {
        events.add(String.format("testFinished %s", description.getDisplayName()));
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        events.add(String.format("testFailure %s", failure.getMessage()));
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        events.add(String.format("testAssumptionFailure %s", failure.getMessage()));
    }

    @Override
    public void testIgnored(Description description) throws Exception {
        events.add(String.format("testIgnored %s", description.getDisplayName()));
    }
}
