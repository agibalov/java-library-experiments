package io.agibalov;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SuperBuilderTest {
    @Test
    public void canConstructFeaturesAndBugs() {
        Feature feature = Feature.builder()
                .id("111")
                .featureDescription("the feature")
                .build();
        assertEquals("111", feature.getId());
        assertEquals("the feature", feature.getFeatureDescription());

        Bug bug = Bug.builder()
                .id("222")
                .bugDescription("the bug")
                .build();
        assertEquals("222", bug.getId());
        assertEquals("the bug", bug.getBugDescription());
    }

    @SuperBuilder
    @Getter
    public static abstract class Ticket {
        private String id;
    }

    @SuperBuilder
    @Getter
    public static class Feature extends Ticket {
        private String featureDescription;
    }

    @SuperBuilder
    @Getter
    public static class Bug extends Ticket {
        private String bugDescription;
    }
}
