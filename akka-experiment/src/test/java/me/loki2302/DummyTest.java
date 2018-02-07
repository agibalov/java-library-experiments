package me.loki2302;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import org.junit.Test;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class DummyTest {
    private final static Duration SHUTDOWN_DURATION = Duration.create(5, TimeUnit.SECONDS);

    @Test
    public void canAskActor() throws Exception {
        ActorSystem system = ActorSystem.create("DummyActorSystem");
        try {
            ActorRef actorRef = system.actorOf(Props.create(HelloActor.class), "hello-actor");

            Timeout timeout = new Timeout(Duration.create(5, TimeUnit.SECONDS));
            Future<Object> resultFuture = Patterns.ask(actorRef, "qwerty", timeout);
            Object resultObject = Await.result(resultFuture, timeout.duration());
            assertEquals("Hi qwerty", resultObject);
        } finally {
            Await.result(system.terminate(), SHUTDOWN_DURATION);
        }
    }
}
