package me.loki2302.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class ObviousIntegrationTest {
    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private Calculator calculator;

    @Test
    public void authorizationServiceSaysLoki2302IsOk() {
        assertTrue(authorizationService.isAuthorized("loki2302"));
    }

    @Test
    public void authorizationServiceSaysAndreyIsNotOk() {
        assertFalse(authorizationService.isAuthorized("andrey"));
    }

    @Test
    public void calculatorAddsNumbersWhenLoki2302AsksForIt() {
        assertEquals(5, calculator.addNumbers("loki2302", 2, 3));
    }

    @Test
    public void calculatorDoesNotAddNumbersWhenAndreyAsksForIt() {
        try {
            calculator.addNumbers("andrey", 2, 3);
            fail();
        } catch (NotAuthorizedException e) {
        }
    }
}
