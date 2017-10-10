package me.loki2302;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutionException;
import org.jdeferred.AlwaysCallback;
import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.Promise.State;

public class App {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final Exchanger<String> exchanger = new Exchanger<String>();
        
        AsyncCalculator asyncProcessor = new AsyncCalculator(new Calculator());
        try {            
            asyncProcessor.addNumbers(1, 2).done(new DoneCallback<Integer>() {
                public void onDone(Integer result) {
                    System.out.printf("Result is %d\n", result);
                    try {
                        exchanger.exchange("ok");
                    } catch (InterruptedException e) {}
                }                
            }).fail(new FailCallback<RuntimeException>() {
                public void onFail(RuntimeException result) {
                    System.out.printf("There was an error: %s\n", result);
                    try {
                        exchanger.exchange("error");
                    } catch (InterruptedException e) {}
                }                
            }).always(new AlwaysCallback<Integer, RuntimeException>() {
                public void onAlways(State state, Integer resolved, RuntimeException rejected) {
                    System.out.printf("Processing finished\n");                    
                }                
            });           
        } finally {
            asyncProcessor.shutdown();
        }
        
        String result = exchanger.exchange(null);
        System.out.println(result);
    }
}
