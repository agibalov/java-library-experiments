package me.loki2302;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class HelloActor extends UntypedActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof String) {
            log.info("Got message {} from {}", message, sender());
            sender().tell("Hi " + message, self());
        } else {
            unhandled(message);
        }
    }
}
