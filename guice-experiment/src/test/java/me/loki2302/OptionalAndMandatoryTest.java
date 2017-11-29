package me.loki2302;

import com.google.inject.*;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionalAndMandatoryTest {
    @Test
    public void optionalIsNotInjectedWhenThereIsNoBinding() {
        Injector injector = Guice.createInjector();
        assertNull(injector.getInstance(ServiceWithOptionalMessage.class).message);
    }

    @Test
    public void optionalIsInjectedWhenThereIsBinding() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(String.class)
                        .annotatedWith(Names.named("message"))
                        .toInstance("hello");
            }
        });
        assertEquals("hello", injector.getInstance(ServiceWithOptionalMessage.class).message);
    }

    @Test
    public void optionalIsNotInjectedWhenThereIsNoBindingAndUsingChildInjector() {
        Injector injector = Guice.createInjector();
        Injector childInjector = injector.createChildInjector();
        assertNull(childInjector.getInstance(ServiceWithOptionalMessage.class).message);
    }

    @Test
    public void optionalIsInjectedWhenThereIsBindingInParentInjector() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(String.class)
                        .annotatedWith(Names.named("message"))
                        .toInstance("hello");
            }
        });
        Injector childInjector = injector.createChildInjector();
        assertEquals("hello", childInjector.getInstance(ServiceWithOptionalMessage.class).message);
    }

    @Test
    @Ignore("https://github.com/google/guice/issues/847")
    public void optionalIsInjectedWhenThereIsBindingInChildInjector() {
        Injector injector = Guice.createInjector();
        Injector childInjector = injector.createChildInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(String.class)
                        .annotatedWith(Names.named("message"))
                        .toInstance("hello");
            }
        });
        assertEquals("hello", childInjector.getInstance(ServiceWithOptionalMessage.class).message);
    }

    // https://github.com/google/guice/issues/847 workaround?
    @Test
    public void optionalIsInjectedWhenThereIsBindingInChildInjectorAndTargetIsRegistered() {
        Injector injector = Guice.createInjector();
        Injector childInjector = injector.createChildInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(String.class)
                        .annotatedWith(Names.named("message"))
                        .toInstance("hello");
                bind(ServiceWithOptionalMessage.class); // !!!
            }
        });
        assertEquals("hello", childInjector.getInstance(ServiceWithOptionalMessage.class).message);
    }

    @Test(expected = ConfigurationException.class)
    public void mandatoryThrowsWhenThereIsNoBinding() {
        Injector injector = Guice.createInjector();
        assertNull(injector.getInstance(ServiceWithMandatoryMessage.class).message);
    }

    @Test
    public void mandatoryIsInjectedWhenThereIsBinding() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(String.class)
                        .annotatedWith(Names.named("message"))
                        .toInstance("hello");
            }
        });
        assertEquals("hello", injector.getInstance(ServiceWithMandatoryMessage.class).message);
    }

    @Test(expected = ConfigurationException.class)
    public void mandatoryThrowsWhenThereIsNoBindingAndUsingChildInjector() {
        Injector injector = Guice.createInjector();
        Injector childInjector = injector.createChildInjector();
        assertNull(childInjector.getInstance(ServiceWithMandatoryMessage.class).message);
    }

    @Test
    public void mandatoryIsInjectedWhenThereIsBindingInParentInjector() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(String.class)
                        .annotatedWith(Names.named("message"))
                        .toInstance("hello");
            }
        });
        Injector childInjector = injector.createChildInjector();
        assertEquals("hello", childInjector.getInstance(ServiceWithMandatoryMessage.class).message);
    }

    @Test
    public void mandatoryIsInjectedWhenThereIsBindingInChildInjector() {
        Injector injector = Guice.createInjector();
        Injector childInjector = injector.createChildInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(String.class)
                        .annotatedWith(Names.named("message"))
                        .toInstance("hello");
            }
        });
        assertEquals("hello", childInjector.getInstance(ServiceWithMandatoryMessage.class).message);
    }

    public static class ServiceWithOptionalMessage {
        @Inject(optional = true)
        @Named("message")
        public String message;
    }

    public static class ServiceWithMandatoryMessage {
        @Inject(optional = false)
        @Named("message")
        public String message;
    }
}
