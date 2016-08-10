package me.loki2302;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class DummyTest {
    @Test
    void oneAndTwoShouldBeThree() {
        assertEquals(3, 1 + 2);
    }

    @TestFactory
    List<DynamicTest> allTheseWordsShouldBe3LetterLong() {
        List<DynamicTest> tests = new ArrayList<>();
        for(String word : new String[] { "age", "you", "hey" }) {
            tests.add(dynamicTest(String.format("%s should be %d letters long", word, 3), () -> {
                assertEquals(3, word.length());
            }));
        }
        return tests;
    }
}
