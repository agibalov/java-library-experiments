package me.loki2302;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AppTest {
    @Autowired
    private ApiFacade apiFacade;

    @Test
    public void dummy() {
        assertEquals(0, apiFacade.countTodos());

        apiFacade.createTodo("1", "hello");

        assertEquals(1, apiFacade.countTodos());
    }
}
