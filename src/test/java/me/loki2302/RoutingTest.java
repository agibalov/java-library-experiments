package me.loki2302;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import akka.testkit.JavaTestKit;
import akka.testkit.TestProbe;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RoutingTest {
    @Test
    public void canUseRouting() {
        ActorSystem system = ActorSystem.create("DummyActorSystem");

        new JavaTestKit(system) {{
            ActorRef actorRef = system.actorOf(Props.create(MasterActor.class), "hello-actor");

            for(int i = 0; i < 5; ++i) {
                TestProbe testProbe = new TestProbe(system);
                ActorRef probeRef = testProbe.ref();
                actorRef.tell("aaa" + i, probeRef);

                String expectedMessage = String.format("OMG! aaa%d akka://DummyActorSystem/user/hello-actor/worker-%d", i, i);
                testProbe.expectMsg(expectedMessage);
            }
        }};

        JavaTestKit.shutdownActorSystem(system);
    }

    public static class MasterActor extends UntypedActor {
        private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
        private Router router;

        @Override
        public void preStart() throws Exception {
            log.info("preStart()");

            List<Routee> routees = new ArrayList<>();
            for(int i = 0; i < 5; ++i) {
                ActorRef workerActorRef = getContext().actorOf(Props.create(WorkerActor.class), "worker-" + i);
                getContext().watch(workerActorRef);
                routees.add(new ActorRefRoutee(workerActorRef));
            }
            router = new Router(new RoundRobinRoutingLogic(), routees);
        }

        @Override
        public void onReceive(Object message) throws Throwable {
            if(message instanceof String) {
                log.info("Got message {}. Routing it. {}", message, self().path());
                router.route(message, sender());
            } else {
                unhandled(message);
            }
        }

        @Override
        public void postStop() throws Exception {
            log.info("postStop()");
        }
    }

    public static class WorkerActor extends UntypedActor {
        private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

        @Override
        public void onReceive(Object message) throws Throwable {
            if(message instanceof String) {
                log.info("Message! {}", message);
                sender().tell("OMG! " + message + " " + self().path(), self());
            } else {
                unhandled(message);
            }
        }
    }
}
