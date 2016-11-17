package me.loki2302;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

public class AppTest {
    private final static Random RANDOM = new Random();

    @Rule
    public ContiPerfRule contiPerfRule = new ContiPerfRule();

    @PerfTest(invocations = 100)
    @Required(max = 140)
    @Test
    public void thisTestAlwaysFails() throws InterruptedException {
        Thread.sleep(50 + RANDOM.nextInt(100));
    }

    @PerfTest(invocations = 100)
    @Required(max = 100)
    @Test
    public void thisTestDoesNotFailOnMyMachine() throws InterruptedException {
        Thread.sleep(30 + RANDOM.nextInt(60));
    }
}
