package me.loki2302.performance;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PerformanceTest {
    @Rule
    public final Meter meter = new Meter();

    @Test
    public void thisTestDoesNotFailOnMyMachine() {
        Random random = new Random();

        Measurement measurement = meter.measure("statementOne", 100, () -> {
            Thread.sleep(30 + random.nextInt(60));
        });

        assertThat(measurement.getMean(), between(50, 70));
    }

    @Test
    public void thisTestAlwaysFails() {
        Random random = new Random();

        Measurement measurement = meter.measure("statementOne", 100, () -> {
            Thread.sleep(30 + random.nextInt(60));
        });

        assertThat(measurement.getMean(), equalTo(100));
    }

    private static Matcher<Long> between(long low, long high) {
        return Matchers.allOf(greaterThanOrEqualTo(low), lessThanOrEqualTo(high));
    }
}
