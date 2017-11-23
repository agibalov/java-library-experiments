package me.loki2302;

import org.junit.Test;
import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Category;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;

import static org.junit.Assert.assertEquals;

public class CategoriesTest {
    @Test
    public void canRunOnlySlowTests() {
        JUnitCore jUnitCore = new JUnitCore();

        Request request = Request.aClass(DummyTest.class)
                // has 'Slow' category (no matter what other categories it has)
                .filterWith(Categories.CategoryFilter.include(Slow.class));

        Result result = jUnitCore.run(request);
        assertEquals(2, result.getRunCount());
    }

    @Test
    public void canRunOnlySlowReadOnlyTests() {
        JUnitCore jUnitCore = new JUnitCore();

        Request request = Request.aClass(DummyTest.class)
                // has at least one of 'Slow' and 'ReadOnly'
                .filterWith(Categories.CategoryFilter.include(Slow.class, ReadOnly.class));

        Result result = jUnitCore.run(request);
        assertEquals(2, result.getRunCount());
    }

    public static class DummyTest {
        @Test
        public void dummyFastTest() {
        }

        @Category(Slow.class)
        @Test
        public void dummySlowTest() {
        }

        @Category({Slow.class, ReadOnly.class})
        @Test
        public void dummySlowReadOnlyTest() {
        }
    }

    public interface Slow {}
    public interface ReadOnly {}
}
