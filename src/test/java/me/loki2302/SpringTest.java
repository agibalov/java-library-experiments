package me.loki2302;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SpringTest {
    @Configuration
    static class Config {
        @Bean
        public Calculator calculator() {
            return mock(Calculator.class);
        }
    }

    @Autowired
    private Calculator calculator;

    @Test
    public void dummy() {
        when(calculator.addNumbers(2, 3)).thenReturn(5);

        int result = calculator.addNumbers(2, 3);
        assertEquals(5, result);

        verify(calculator, only()).addNumbers(2, 3);
    }

    public static interface Calculator {
        int addNumbers(int a, int b);
    }
}
