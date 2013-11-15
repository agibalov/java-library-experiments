package me.loki2302;

import static org.junit.Assert.*;

import java.util.concurrent.Exchanger;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.junit.Test;

public class AsyncCalculatorTest {
    @Test
    public void canHandleSuccess() throws InterruptedException {
        final Exchanger<Integer> resultExchanger = new Exchanger<Integer>();
        
        AsyncCalculator asyncProcessor = new AsyncCalculator(new Calculator());
        try {            
            asyncProcessor.addNumbers(1, 2).done(new DoneCallback<Integer>() {
                public void onDone(Integer result) {
                    try {
                        resultExchanger.exchange(result);
                    } catch (InterruptedException e) {}
                }                
            });           
        } finally {
            asyncProcessor.shutdown();
        }
        
        Integer result = resultExchanger.exchange(null);
        assertEquals(3, (int)result);
    }
    
    @Test
    public void canHandleFailure() throws InterruptedException {
        final Exchanger<RuntimeException> resultExchanger = new Exchanger<RuntimeException>();
        
        AsyncCalculator asyncProcessor = new AsyncCalculator(new Calculator());
        try {            
            asyncProcessor.addNumbers(1, 20).fail(new FailCallback<RuntimeException>() {
                @Override
                public void onFail(RuntimeException result) {
                    try {
                        resultExchanger.exchange(result);
                    } catch (InterruptedException e) {}                    
                }                
            });           
        } finally {
            asyncProcessor.shutdown();
        }
        
        RuntimeException result = resultExchanger.exchange(null);
        assertTrue(result instanceof IllegalArgumentException);
    }
}