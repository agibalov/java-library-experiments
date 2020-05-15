package io.agibalov;

import lombok.val;
import lombok.var;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ValVarTest {
    @Test
    public void canUseVal() {
        val todos = new ArrayList<String>();
        todos.add("get some milk");
        todos.add("and drink it");
        assertEquals(ArrayList.class, todos.getClass());

        dummyMethodThatAcceptsString(todos.get(0));
        dummyMethodThatAcceptsListOfStrings(todos);

        // NOTE: cannot reassign, because it's VAL, not VAR
        // todos = new ArrayList<>();
    }

    @Test
    public void canUseVar() {
        var todos = new ArrayList<String>();
        todos.add("get some milk");
        todos.add("and drink it");
        assertEquals(ArrayList.class, todos.getClass());

        dummyMethodThatAcceptsString(todos.get(0));
        dummyMethodThatAcceptsListOfStrings(todos);

        // NOTE: I don't specify the type
        todos = new ArrayList<>();
        todos.add("xxx");
        dummyMethodThatAcceptsString(todos.get(0));
        dummyMethodThatAcceptsListOfStrings(todos);
    }

    private void dummyMethodThatAcceptsListOfStrings(List<String> strings) {
    }

    private void dummyMethodThatAcceptsString(String s) {
    }
}
