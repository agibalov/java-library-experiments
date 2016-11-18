package me.loki2302.performance;

import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AppTest {
    @Rule
    public final Meter meter = new Meter();

    @Test
    public void thisTestDoesNotFailOnMyMachine() {
        Random random = new Random();

        Measurement measurement = meter.measure("statementOne", 100, () -> {
            Thread.sleep(30 + random.nextInt(60));
        });

        assertTrue(measurement.getMean() > 50 && measurement.getMean() < 70);
    }

    @Test
    public void thisTestAlwaysFails() {
        Random random = new Random();

        Measurement measurement = meter.measure("statementOne", 100, () -> {
            Thread.sleep(30 + random.nextInt(60));
        });

        assertEquals(100, measurement.getMean());
    }
}
