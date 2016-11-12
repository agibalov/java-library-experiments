package me.loki2302;

import me.loki2302.domain.exceptions.BusinessException;
import me.loki2302.domain.exceptions.TodoAlreadyExistsException;
import me.loki2302.domain.exceptions.TodoCountLimitExceededException;
import me.loki2302.domain.exceptions.TodoNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
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

        apiFacade.createTodo("1", "hello");

        assertEquals(1, apiFacade.countTodos());
        assertEquals(1, apiFacade.countTodos2());
    }

    @Test(expected = TodoAlreadyExistsException.class)
    public void createShouldThrowIfTodoAlreadyExists() throws BusinessException {
        apiFacade.createTodo("111", "hi");
        apiFacade.createTodo("111", "bye");
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
        apiFacade.createTodo("1", "hello");
        apiFacade.createTodo("2", "world");
        apiFacade.createTodo("3", "!!!");

        try {
            apiFacade.createTodo("4", "fail");
            fail();
        } catch (TodoCountLimitExceededException e) {
            // expected
        }
    }
}
