package io.agibalov;

import lombok.experimental.Delegate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DelegateTest {
    @Test
    public void canUseDelegation() {
        TodoList todoList = new TodoList();

        todoList.add("get some milk");
        assertEquals(1, todoList.size());

        todoList.add("get some more milk");
        assertEquals(2, todoList.size());
    }

    public static class TodoList {
        private interface SimplifiedCollection {
            boolean add(String item);
            int size();
        }

        @Delegate(types = SimplifiedCollection.class)
        private final List<String> items = new ArrayList<>();
    }
}
