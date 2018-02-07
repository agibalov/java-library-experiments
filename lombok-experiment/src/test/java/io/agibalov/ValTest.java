package io.agibalov;

import lombok.val;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ValTest {
    @Test
    public void dummy() {
        val todos = new ArrayList<String>();
        todos.add("get some milk");
        todos.add("and drink it");
        assertEquals(ArrayList.class, todos.getClass());

        dummyMethodThatAcceptsString(todos.get(0));
        dummyMethodThatAcceptsListOfStrings(todos);
    }

    private void dummyMethodThatAcceptsListOfStrings(List<String> strings) {
    }

    private void dummyMethodThatAcceptsString(String s) {
    }
}
