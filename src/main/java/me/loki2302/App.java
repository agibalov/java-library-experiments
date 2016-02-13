package me.loki2302;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerAdapter;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.fs.FileSystemEventStore;
import org.axonframework.eventstore.fs.SimpleEventFileResolver;

import java.io.File;
import java.util.UUID;

public class App {
    public static void main(String[] args) {
        CommandBus commandBus = new SimpleCommandBus();
        CommandGateway commandGateway = new DefaultCommandGateway(commandBus);
        EventStore eventStore = new FileSystemEventStore(new SimpleEventFileResolver(new File("./events")));
        EventBus eventBus = new SimpleEventBus();
        EventSourcingRepository eventSourcingRepository = new EventSourcingRepository(Todo.class, eventStore);
        eventSourcingRepository.setEventBus(eventBus);

        AggregateAnnotationCommandHandler.subscribe(Todo.class, eventSourcingRepository, commandBus);

        AnnotationEventListenerAdapter.subscribe(new Object() {
            @EventHandler
            public void handle(TodoCreatedEvent e) {
                System.out.printf("todo created: %s %s\n", e.todoId, e.text);
            }
        }, eventBus);

        CreateTodoCommand c = new CreateTodoCommand();
        c.todoId = UUID.randomUUID().toString();
        c.text = "hi there";
        commandGateway.send(c);
    }

    public static class CreateTodoCommand {
        @TargetAggregateIdentifier
        public String todoId;
        public String text;
    }

    public static class TodoCreatedEvent {
        public String todoId;
        public String text;
    }

    public static class Todo extends AbstractAnnotatedAggregateRoot {
        @AggregateIdentifier
        public String id;

        public Todo() {
        }

        @CommandHandler
        public Todo(CreateTodoCommand command) {
            TodoCreatedEvent e = new TodoCreatedEvent();
            e.todoId = command.todoId;
            e.text = command.text;
            apply(e);
        }

        @EventHandler
        public void on(TodoCreatedEvent e) {
            this.id = e.todoId;
        }
    }
}
