package me.loki2302;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockitoTest {
    @Test
    public void canHaveAMockImplementationForInterface() {
        SomeDataProvider someDataProvider = mock(SomeDataProvider.class);
        when(someDataProvider.provideSomeData()).thenReturn("hello there");

        assertEquals("hello there", someDataProvider.provideSomeData());
    }

    public static interface SomeDataProvider {
        String provideSomeData();
    }
}
