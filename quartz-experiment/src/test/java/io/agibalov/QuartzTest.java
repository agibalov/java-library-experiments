package io.agibalov;

import org.junit.Test;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import java.time.Instant;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class QuartzTest {
    @Test
    public void canCalculateWhenEventShouldHappen() {
        Instant i = Instant.parse("2018-01-01T00:00:00.000Z");

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(TriggerKey.triggerKey("xxx"))
                .startAt(Date.from(i))
                .withSchedule(SimpleScheduleBuilder
                        .repeatSecondlyForever(13)
                        .repeatForever())
                .build();

        assertEquals(i.plusSeconds(13), trigger.getFireTimeAfter(Date.from(i)).toInstant());
    }
}
