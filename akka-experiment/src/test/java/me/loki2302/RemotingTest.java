package me.loki2302;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.Test;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class RemotingTest {
    private final static Duration SHUTDOWN_DURATION = Duration.create(5, TimeUnit.SECONDS);

    @Test
    public void canUseRemoting() throws Exception {
        Config remoteConfig = ConfigFactory.parseFile(new File("remote.conf"));
        ActorSystem remoteSystem = null;
        try {
            remoteSystem = ActorSystem.create("RemoteSystem", remoteConfig);
            ActorRef actorRef = remoteSystem.actorOf(Props.create(HelloActor.class), "remote");
            System.out.printf("Local reference to actor: %s\n", actorRef);

            Config localConfig = ConfigFactory.parseFile(new File("local.conf"));
            ActorSystem localSystem = null;
            try {
                localSystem = ActorSystem.create("LocalSystem", localConfig);
                ActorSelection actorSelection = localSystem.actorSelection("akka.tcp://RemoteSystem@127.0.0.1:5150/user/remote");

                Timeout timeout = new Timeout(Duration.create(5, TimeUnit.SECONDS));
                Future<Object> resultFuture = Patterns.ask(actorSelection, "qwerty", timeout);
                Object resultObject = Await.result(resultFuture, timeout.duration());
                assertEquals("Hi qwerty", resultObject);
            } finally {
                Await.result(localSystem.terminate(), SHUTDOWN_DURATION);
            }
        } finally {
            Await.result(remoteSystem.terminate(), SHUTDOWN_DURATION);
        }
    }
}
