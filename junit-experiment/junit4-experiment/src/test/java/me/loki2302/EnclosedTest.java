package me.loki2302;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(Enclosed.class)
public class EnclosedTest {
    public static class InnerTest1 {
        @Test
        public void test1() {
            assertTrue(true);
        }

        @Test
        public void test2() {
            assertTrue(true);
        }
    }

    public static class InnerTest2 {
        @Test
        public void test1() {
            assertTrue(true);
        }

        @Test
        public void test2() {
            assertTrue(true);
        }
    }
}
