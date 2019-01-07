package me.loki2302;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockingDetails;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MockitoArgumentCaptureTest {
    @Test
    public void canUseArgumentCaptor() {
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

    @Test
    public void canUseMockingDetails() {
        SomeService someService = mock(SomeService.class);
        doNothing().when(someService).doSomething(any());

        someService.doSomething("aaa");
        someService.doSomething("bbb");
        someService.doSomething("ccc");

        MockingDetails mockingDetails = Mockito.mockingDetails(someService);
        System.out.println(mockingDetails.printInvocations());

        List<Invocation> invocations = StreamSupport.stream(mockingDetails.getInvocations().spliterator(), false)
                .collect(Collectors.toList());
        assertEquals(3, invocations.size());
        assertArrayEquals(new Object[] { "aaa" }, invocations.get(0).getRawArguments());
        assertArrayEquals(new Object[] { "bbb" }, invocations.get(1).getRawArguments());
        assertArrayEquals(new Object[] { "ccc" }, invocations.get(2).getRawArguments());
    }

    public static class SomeService {
        public void doSomething(String what) {
        }
    }
}
