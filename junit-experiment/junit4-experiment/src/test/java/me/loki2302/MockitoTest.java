package me.loki2302;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class MockitoTest {
    @Test
    public void canAddNumbersWhenUserIsAuthenticated() {
        AuthorizationService authorizationService = mock(AuthorizationService.class);
        when(authorizationService.isAuthorized("loki2302")).thenReturn(true);

        Calculator calculator = new Calculator(authorizationService);
        assertEquals(5, calculator.addNumbers("loki2302", 2, 3));

        verify(authorizationService, times(1)).isAuthorized("loki2302");
    }

    @Test
    public void cantAddNumbersWhenUserIsNotAuthenticated() {
        AuthorizationService authorizationService = mock(AuthorizationService.class);
        when(authorizationService.isAuthorized("loki2302")).thenReturn(false);

        Calculator calculator = new Calculator(authorizationService);
        try {
            calculator.addNumbers("loki2302", 2, 3);
            fail();
        } catch (NotAuthorizedException e) {
        }

        verify(authorizationService, times(1)).isAuthorized("loki2302");
    }

    @Test
    public void calculatorChecksAuthorizationBeforeAddingNumbers() {
        AuthorizationService authorizationService = mock(AuthorizationService.class);
        when(authorizationService.isAuthorized(anyString())).thenReturn(true);

        Calculator calculator = new Calculator(authorizationService);
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
