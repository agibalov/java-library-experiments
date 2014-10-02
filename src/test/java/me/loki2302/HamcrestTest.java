package me.loki2302;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class HamcrestTest {
    @Test
    public void canUseBuiltInMatchers() {
        List<Integer> items = Arrays.asList(1, 2, 3);
        assertThat(items, is(not(empty())));
        assertThat(items, hasSize(3));
        assertThat(items, hasSize(lessThanOrEqualTo(3)));
        assertThat(items, everyItem(lessThanOrEqualTo(3)));
        assertThat(items, everyItem(greaterThanOrEqualTo(1)));
        assertThat(items, everyItem(allOf(greaterThanOrEqualTo(1), lessThanOrEqualTo(3))));
        assertThat(items, hasItem(equalTo(1)));
        assertThat(items, hasItem(greaterThan(2)));
        assertThat(items, hasItems(2, 3, 1));
        assertThat(items, containsInAnyOrder(2, 3, 1));
        assertThat(items, containsInAnyOrder(equalTo(2), greaterThan(2), lessThan(2)));
        assertThat(items, contains(1, 2, 3));
        assertThat(items, contains(lessThan(2), equalTo(2), greaterThan(2)));
    }

    @Test
    public void canUseCustomMatcher() {
        assertThat(Arrays.asList(2, 4, 6), everyItem(isEven()));
        assertThat(Arrays.asList(1, 3, 5), everyItem(not(isEven())));
    }

    public static IsEvenMatcher isEven() {
        return new IsEvenMatcher();
    }

    public static class IsEvenMatcher extends TypeSafeMatcher<Integer> {
        @Override
        protected boolean matchesSafely(Integer item) {
            return item % 2 == 0;
        }

        @Override
        public void describeTo(Description description) {
            // this results in "[every item should be] even"
            description.appendText("even");
        }
    }
}
