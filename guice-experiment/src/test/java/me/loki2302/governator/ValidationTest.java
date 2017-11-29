package me.loki2302.governator;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.netflix.governator.guice.LifecycleInjector;
import com.netflix.governator.lifecycle.LifecycleManager;
import com.netflix.governator.lifecycle.ValidationException;
import org.junit.Test;

import javax.validation.constraints.Min;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ValidationTest {
    @Test
    public void canUseValidation() throws Exception {
        Injector injector = LifecycleInjector.builder().withModules(new AbstractModule() {
            @Override
            protected void configure() {
                bind(Dummy.class).asEagerSingleton();
                bindConstant().annotatedWith(Names.named("x")).to(9);
            }
        }).build().createInjector();

        LifecycleManager lifecycleManager = injector.getInstance(LifecycleManager.class);
        try {
            lifecycleManager.start();
            fail();
        } catch (ValidationException e) {
            assertTrue(e.getMessage().contains("Dummy.x"));
        }
    }

    public static class Dummy {
        @Inject
        @Named("x")
        @Min(10)
        public int x;
    }
}
