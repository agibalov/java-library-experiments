package me.loki2302.governator;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.netflix.governator.guice.LifecycleInjector;
import com.netflix.governator.lifecycle.LifecycleManager;
import org.junit.Test;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.junit.Assert.assertEquals;

public class LifecycleManagementTest {
    @Test
    public void canUsePostConstructAndPreDestroy() throws Exception {
        Injector injector = LifecycleInjector.builder()
                .withModules(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(DummyService.class).asEagerSingleton();
                    }
                })
                .build()
                .createInjector();

        LifecycleManager lifecycleManager = injector.getInstance(LifecycleManager.class);
        lifecycleManager.start();

        DummyService dummyService = injector.getInstance(DummyService.class);
        assertEquals(DummyServiceState.Initialized, dummyService.state);

        lifecycleManager.close();
        assertEquals(DummyServiceState.CleanedUp, dummyService.state);
    }

    public static class DummyService {
        public DummyServiceState state = DummyServiceState.New;

        @PostConstruct
        public void initialize() {
            state = DummyServiceState.Initialized;
        }

        @PreDestroy
        public void cleanUp() {
            state = DummyServiceState.CleanedUp;
        }
    }

    private static enum DummyServiceState {
        New,
        Initialized,
        CleanedUp
    }
}
