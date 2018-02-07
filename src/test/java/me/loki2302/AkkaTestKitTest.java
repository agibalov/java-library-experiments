package me.loki2302;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import org.junit.Test;

public class AkkaTestKitTest {
    @Test
    public void canUseAkkaTestKit() {
        ActorSystem system = ActorSystem.create("DummyActorSystem");

        new JavaTestKit(system) {{
            ActorRef actorRef = system.actorOf(Props.create(HelloActor.class), "hello-actor");
            ActorRef probeRef = getRef();
            actorRef.tell("qwerty", probeRef);

            expectMsgEquals("Hi qwerty");
        }};

        JavaTestKit.shutdownActorSystem(system);
    }
}
