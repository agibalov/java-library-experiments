package me.loki2302;

import com.google.inject.*;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class CustomInjectionTest {
    @Test
    public void canUseCustomInjection() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bindListener(Matchers.any(), new MagicValueTypeListener());
            }
        });

        assertEquals("true magic", injector.getInstance(Service.class).magicValue);
    }

    public static class Service {
        @InjectMagicValue
        public String magicValue;
    }

    @Retention(RetentionPolicy.RUNTIME)
    public static @interface InjectMagicValue {
    }

    public static class MagicValueTypeListener implements TypeListener {
        @Override
        public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
            for(Field field : type.getRawType().getDeclaredFields()) {
                if(field.getType() == String.class && field.isAnnotationPresent(InjectMagicValue.class)) {
                    encounter.register(new MagicValueMembersInjector<I>(field));
                }
            }
        }
    }

    public static class MagicValueMembersInjector<T> implements MembersInjector<T> {
        private final Field field;

        public MagicValueMembersInjector(Field field) {
            this.field = field;
        }

        @Override
        public void injectMembers(T instance) {
            field.setAccessible(true);
            try {
                field.set(instance, "true magic");
            } catch(IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
