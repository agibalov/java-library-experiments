package me.loki2302;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.matcher.Matchers;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AopTest {
    @Test
    public void canUseAop() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(DummyService.class).asEagerSingleton();
                bind(AuditorService.class).asEagerSingleton();
                bindInterceptor(
                        Matchers.only(DummyService.class),
                        Matchers.any(),
                        new AuditingMethodInterceptor(getProvider(AuditorService.class)));
            }
        });

        DummyService dummyService = injector.getInstance(DummyService.class);
        dummyService.ping();

        AuditorService auditorService = injector.getInstance(AuditorService.class);
        List<String> events = auditorService.getEvents();
        assertEquals(2, events.size());
        assertEquals("before:ping", events.get(0));
        assertEquals("after:ping", events.get(1));
    }

    public static class DummyService {
        public void ping() {
            System.out.println("ping()");
        }
    }

    public static class AuditorService {
        private final List<String> events = new ArrayList<String>();

        public void addEvent(String event) {
            events.add(event);
        }

        public List<String> getEvents() {
            return events;
        }
    }

    public static class AuditingMethodInterceptor implements MethodInterceptor {
        private final Provider<AuditorService> auditorServiceProvider;

        public AuditingMethodInterceptor(Provider<AuditorService> auditorServiceProvider) {
            this.auditorServiceProvider = auditorServiceProvider;
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            AuditorService auditorService = auditorServiceProvider.get();

            String methodName = invocation.getMethod().getName();

            auditorService.addEvent("before:" + methodName);
            Object result = invocation.proceed();
            auditorService.addEvent("after:" + methodName);
            return result;
        }
    }
}
