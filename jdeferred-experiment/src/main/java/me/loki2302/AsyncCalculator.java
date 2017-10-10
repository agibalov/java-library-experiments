package me.loki2302;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import org.jdeferred.Deferred;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;

public class AsyncCalculator {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Calculator calculator;
    
    public AsyncCalculator(Calculator calculator) {
        this.calculator = calculator;
    }
    
    public Promise<Integer, RuntimeException, Void> addNumbers(final int a, final int b) {
        final Deferred<Integer, RuntimeException, Void> deferred = 
                new DeferredObject<Integer, RuntimeException, Void>();
        
        executor.submit(new Runnable() {
            public void run() {
                try {
                    int result = calculator.addNumbers(a, b);                        
                    deferred.resolve(result);
                } catch(RuntimeException e) {
                    deferred.reject(e);
                }
            }                
        });
        
        return deferred.promise();
    }        
    
    public void shutdown() {
        executor.shutdown();
    }
}