package me.loki2302;

import static org.junit.Assert.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.ScopeAnnotation;

public class CustomInjectionScopeTest {
    @Test
    public void canUseCustomInjectionScope() {
        Injector injector = Guice.createInjector(new RequestScopedCalculatorModule());

        RequestProcessor requestProcessor = injector.getInstance(RequestProcessor.class);
        int result = requestProcessor.processRequest(request(1, 2));
        assertEquals(3, result);
    }

    private static Request request(int a, int b) {
        Request request = new Request();
        request.a = a;
        request.b = b;
        return request;
    }

    public static class RequestScopedCalculatorModule implements Module {
        @Override
        public void configure(Binder binder) {
            RequestScope requestScope = new RequestScope();
            binder.bindScope(RequestScoped.class, requestScope);
            binder.bind(RequestScope.class).toInstance(requestScope);
        }
    }

    public static class RequestProcessor {
        @Inject
        private RequestScope requestScope;

        @Inject
        private Injector injector;

        public int processRequest(Request request) {
            requestScope.enter();
            try {
                requestScope.setRequest(request);

                SumCalculator sumCalculator = injector.getInstance(SumCalculator.class);
                sumCalculator.addNumbers();

                // has already been created while resolving SumCalculator ^^^
                Response response = injector.getInstance(Response.class);

                return response.result;
            } finally {
                requestScope.exit();
            }
        }
    }

    @RequestScoped
    // otherwise it will not even consider the scope, will just create a new one
    // as usual
    public static class Request {
        public int a;
        public int b;
    }

    @RequestScoped
    public static class Response {
        public int result;
    }

    @RequestScoped
    public static class SumCalculator {
        @Inject
        private Request request;

        @Inject
        private Response response;

        public void addNumbers() {
            response.result = request.a + request.b;
        }
    }

    @Target({ ElementType.TYPE, ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @ScopeAnnotation
    public static @interface RequestScoped {
    }

    public static class RequestScope implements Scope {
        private final ThreadLocal<Map<Key<?>, Object>> values = new ThreadLocal<Map<Key<?>, Object>>();

        public void enter() {
            values.set(new HashMap<Key<?>, Object>());
        }

        public void exit() {
            values.remove();
        }

        public void setRequest(Request request) {
            Map<Key<?>, Object> scopedObjects = values.get();
            scopedObjects.put(Key.get(Request.class), request);
        }

        @Override
        public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
            return new Provider<T>() {
                @Override
                public T get() {
                    Map<Key<?>, Object> scopedObjects = values.get();

                    @SuppressWarnings("unchecked")
                    T current = (T) scopedObjects.get(key);
                    if (current == null) {
                        current = unscoped.get();
                        scopedObjects.put(key, current);
                    }

                    return current;
                }
            };
        }
    }
}