package me.loki2302;

import me.loki2302.domain.exceptions.BusinessException;
import me.loki2302.domain.exceptions.TodoCountLimitExceededException;
import me.loki2302.domain.exceptions.TodoNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
public class AppTest {
    @Autowired
    private ApiFacade apiFacade;

    @Test
    public void demo() throws BusinessException {
        assertEquals(0, apiFacade.countTodos());
        assertEquals(0, apiFacade.countTodos2());

        apiFacade.createTodo("hello");

        assertEquals(1, apiFacade.countTodos());
        assertEquals(1, apiFacade.countTodos2());
    }

    @Test
    public void createShouldReturnTodoId() throws BusinessException {
        String id = apiFacade.createTodo("hello");
        assertNotNull(id);
    }

    @Test(expected = TodoNotFoundException.class)
    public void updateShouldThrowIfTodoDoesNotExist() throws BusinessException {
        apiFacade.updateTodo("111", "hi");
    }

    @Test(expected = TodoNotFoundException.class)
    public void deleteShouldThrowIfTodoDoesNotExist() throws BusinessException {
        apiFacade.deleteTodo("111");
    }

    @Test
    public void createShouldThrowIfTodoCountIsExceeded() throws BusinessException {
        apiFacade.createTodo("hello");
        apiFacade.createTodo("world");
        apiFacade.createTodo("!!!");

        try {
            apiFacade.createTodo("fail");
            fail();
        } catch (TodoCountLimitExceededException e) {
            // expected
        }
    }
}
