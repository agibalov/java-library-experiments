package me.loki2302;

import static org.junit.Assert.assertEquals;
import me.loki2302.CustomInjectionScopeTest.RequestScopedCalculatorModule;

import org.junit.Test;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;

public class ChildInjectorTest {
    @Test
    public void canUseChildInjectorToSimulateInjectionScope() {
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
    
    @Singleton
    public static class RequestProcessor {        
        @Inject
        private Injector injector;

        public int processRequest(Request request) {
            Injector requestScopedInjector = injector.createChildInjector(new RequestScopeExtenderModule(request));
            
            SumCalculator sumCalculator = requestScopedInjector.getInstance(SumCalculator.class);
            sumCalculator.addNumbers();
            
            Response response = requestScopedInjector.getInstance(Response.class);

            return response.result;
        }
        
        private static class RequestScopeExtenderModule implements Module {
            private final Request request;
            
            public RequestScopeExtenderModule(Request request) {
                this.request = request;
            }
            
            @Override
            public void configure(Binder binder) {
                binder.bind(Request.class).toInstance(request);
                binder.bind(Response.class).asEagerSingleton();
            }        
        }
    }
    
    public static class Request {
        public int a;
        public int b;
    }
    
    public static class Response {
        public int result;
    }
    
    public static class SumCalculator {
        @Inject
        private Request request;

        @Inject
        private Response response;

        public void addNumbers() {
            response.result = request.a + request.b;
        }
    }
}