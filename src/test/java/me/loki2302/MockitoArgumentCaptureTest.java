package me.loki2302;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MockitoArgumentCaptureTest {
    @Test
    public void canCaptureArguments() {
        SomeService someService = mock(SomeService.class);
        doNothing().when(someService).doSomething(any());

        someService.doSomething("aaa");
        someService.doSomething("bbb");
        someService.doSomething("ccc");

        ArgumentCaptor<String> whatCaptor = ArgumentCaptor.forClass(String.class);
        verify(someService, times(3)).doSomething(whatCaptor.capture());
        assertEquals(3, whatCaptor.getAllValues().size());
        assertEquals("aaa", whatCaptor.getAllValues().get(0));
        assertEquals("bbb", whatCaptor.getAllValues().get(1));
        assertEquals("ccc", whatCaptor.getAllValues().get(2));
    }

    public static class SomeService {
        public void doSomething(String what) {
        }
    }
}
