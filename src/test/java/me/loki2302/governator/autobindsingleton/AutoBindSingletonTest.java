package me.loki2302.governator.autobindsingleton;

import com.google.inject.Injector;
import com.netflix.governator.guice.LifecycleInjector;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AutoBindSingletonTest {
    @Test
    public void canAutoBindAsEagerSingleton() {
        Injector injector = LifecycleInjector.builder()
                .usingBasePackages(AutoBindSingletonTest.class.getPackage().getName())
                .build()
                .createInjector();

        DummyService dummyService1 = injector.getInstance(DummyService.class);
        DummyService dummyService2 = injector.getInstance(DummyService.class);

        assertTrue(dummyService1 == dummyService2);
    }
}
