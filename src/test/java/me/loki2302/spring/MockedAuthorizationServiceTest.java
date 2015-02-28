package me.loki2302.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

// TODO: how do I use the AppConfig by default, but override the specific beans?

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MockedAuthorizationServiceTest {
    @Configuration
    static class Config {
        @Bean
        public AuthorizationService authorizationService() {
            return mock(AuthorizationService.class);
        }

        @Bean
        public Calculator calculator() {
            return new Calculator();
        }
    }

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private Calculator calculator;

    @Test
    public void whenAuthorizationServiceSaysThatLoki2302IsOkCalculatorAddsNumbers() {
        when(authorizationService.isAuthorized("loki2302")).thenReturn(true);

        assertEquals(5, calculator.addNumbers("loki2302", 2, 3));

        verify(authorizationService, only()).isAuthorized("loki2302");
    }

    @Test
    public void whenAuthorizationServiceSaysThatLoki2302IsNotOkCalculatorThrows() {
        when(authorizationService.isAuthorized("loki2302")).thenReturn(false);

        try {
            calculator.addNumbers("loki2302", 2, 3);
            fail();
        } catch (NotAuthorizedException e) {
        }

        verify(authorizationService, only()).isAuthorized("loki2302");
    }
}
