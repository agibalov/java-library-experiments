package me.loki2302;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DummyTest {
    @Test
    public void dummy() throws InterruptedException {
        MyServer myServer = new MyServer();
        myServer.start();
        try {
            MyClient myClient = new MyClient();
            myClient.start();
            try {
                int result = myClient.addNumbers(123, 1);
                assertEquals(124, result);
            } finally {
                myClient.stop();
            }
        } finally {
            myServer.stop();
        }
    }
}
