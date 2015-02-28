package me.loki2302;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MockitoJUnitRunnerTest {
    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private Calculator calculator;

    @Test
    public void canAddNumbersWhenUserIsAuthenticated() {
        when(authorizationService.isAuthorized("loki2302")).thenReturn(true);

        assertEquals(5, calculator.addNumbers("loki2302", 2, 3));

        verify(authorizationService, times(1)).isAuthorized("loki2302");
    }

    @Test
    public void cantAddNumbersWhenUserIsNotAuthenticated() {
        when(authorizationService.isAuthorized("loki2302")).thenReturn(false);

        try {
            calculator.addNumbers("loki2302", 2, 3);
            fail();
        } catch (NotAuthorizedException e) {
        }

        verify(authorizationService, times(1)).isAuthorized("loki2302");
    }

    @Test
    public void calculatorChecksAuthorizationBeforeAddingNumbers() {
        when(authorizationService.isAuthorized(anyString())).thenReturn(true);

        calculator.addNumbers("loki2302", 2, 3);

        verify(authorizationService, times(1)).isAuthorized("loki2302");
    }

    public static interface AuthorizationService {
        boolean isAuthorized(String username);
    }

    public static class Calculator {
        private final AuthorizationService authorizationService;

        public Calculator(AuthorizationService authorizationService) {
            this.authorizationService = authorizationService;
        }

        public int addNumbers(String username, int a, int b) {
            if(!authorizationService.isAuthorized(username)) {
                throw new NotAuthorizedException();
            }

            return a + b;
        }
    }

    public static class NotAuthorizedException extends RuntimeException {
    }
}
