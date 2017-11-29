package me.loki2302;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public class BasicTest {
    @Test
    public void iCanHave2SingletonsOfTheSameClass() {
        final MyService one = new MyService();
        final MyService two = new MyService();

        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(MyService.class).annotatedWith(Names.named("one")).toInstance(one);
                bind(MyService.class).annotatedWith(Names.named("two")).toInstance(two);
            }
        });

        assertSame(one, injector.getInstance(Key.get(MyService.class, Names.named("one"))));
        assertSame(two, injector.getInstance(Key.get(MyService.class, Names.named("two"))));
    }

    private static class MyService {
    }
}
