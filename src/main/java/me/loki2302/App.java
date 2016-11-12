package me.loki2302;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.annotation.AnnotationCommandHandlerBeanPostProcessor;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.CommandGatewayFactoryBean;
import org.axonframework.common.jpa.ContainerManagedEntityManagerProvider;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerBeanPostProcessor;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.jpa.DomainEventEntry;
import org.axonframework.eventstore.jpa.JpaEventStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class App implements CommandLineRunner {
    private final static Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Autowired
    private ApiFacade apiFacade;

    @Override
    public void run(String... args) throws Exception {
        apiFacade.createTodo("1", "hello");

        if(true) {
            long count = apiFacade.coundTodos();
            LOGGER.info("count: {}", count);
        }

        apiFacade.deleteTodo("1");

        if(true) {
            long count = apiFacade.coundTodos();
            LOGGER.info("count: {}", count);
        }
    }

    @Configuration
    @EntityScan(basePackageClasses = { TodoEntity.class, DomainEventEntry.class })
    public static class AxonConfiguration {
        @Bean
        public CommandBus commandBus() {
            return new SimpleCommandBus();
        }

        @Bean
        public CommandGatewayFactoryBean<CommandGateway> commandGatewayFactoryBean() {
            CommandGatewayFactoryBean<CommandGateway> factory = new CommandGatewayFactoryBean<>();
            factory.setCommandBus(commandBus());
            return factory;
        }

        @Bean
        public EventStore eventStore() {
            return new JpaEventStore(entityManagerProvider());
        }

        @Bean
        public EntityManagerProvider entityManagerProvider() {
            return new ContainerManagedEntityManagerProvider();
        }

        @Bean
        public EventBus eventBus() {
            return new SimpleEventBus();
        }

        @Bean
        public EventSourcingRepository todoItemRepository() {
            EventSourcingRepository repository = new EventSourcingRepository(TodoItem.class, eventStore());
            repository.setEventBus(eventBus());
            return repository;
        }

        @Bean
        public AnnotationEventListenerBeanPostProcessor annotationEventListenerBeanPostProcessor() {
            AnnotationEventListenerBeanPostProcessor processor = new AnnotationEventListenerBeanPostProcessor();
            processor.setEventBus(eventBus());
            return processor;
        }

        @Bean
        public AnnotationCommandHandlerBeanPostProcessor annotationCommandHandlerBeanPostProcessor() {
            AnnotationCommandHandlerBeanPostProcessor processor = new AnnotationCommandHandlerBeanPostProcessor();
            processor.setCommandBus(commandBus());
            return processor;
        }

        @Bean
        public AggregateAnnotationCommandHandler<TodoItem> taskCommandHandler() {
            return AggregateAnnotationCommandHandler.subscribe(TodoItem.class, todoItemRepository(), commandBus());
        }
    }

    public static class CreateTodoItemCommand {
        @TargetAggregateIdentifier
        public final String todoId;
        public final String text;

        public CreateTodoItemCommand(String todoId, String text) {
            this.todoId = todoId;
            this.text = text;
        }
    }

    public static class UpdateTodoItemCommand {
        @TargetAggregateIdentifier
        public final String todoId;
        public final String text;

        public UpdateTodoItemCommand(String todoId, String text) {
            this.todoId = todoId;
            this.text = text;
        }
    }

    public static class DeleteTodoItemCommand {
        @TargetAggregateIdentifier
        public final String todoId;

        public DeleteTodoItemCommand(String todoId) {
            this.todoId = todoId;
        }
    }

    public static class TodoItemCreatedEvent {
        public final String todoId;
        public final String text;

        public TodoItemCreatedEvent(String todoId, String text) {
            this.todoId = todoId;
            this.text = text;
        }
    }

    public static class TodoItemUpdatedEvent {
        public final String todoId;
        public final String text;

        public TodoItemUpdatedEvent(String todoId, String text) {
            this.todoId = todoId;
            this.text = text;
        }
    }

    public static class TodoItemDeletedEvent {
        public final String todoId;

        public TodoItemDeletedEvent(String todoId) {
            this.todoId = todoId;
        }
    }

    public static class TodoItem extends AbstractAnnotatedAggregateRoot {
        private final static Logger LOGGER = LoggerFactory.getLogger(TodoItem.class);

        @AggregateIdentifier
        private String id;
        private String text;

        public TodoItem() {
        }

        @CommandHandler
        public TodoItem(CreateTodoItemCommand command) {
            LOGGER.info("in constructor, command={}", command);
            apply(new TodoItemCreatedEvent(command.todoId, command.text));
        }

        @CommandHandler
        public void on(UpdateTodoItemCommand command) {
            LOGGER.info("in update command handler, command={}", command);
            apply(new TodoItemUpdatedEvent(command.todoId, command.text));
        }

        @CommandHandler
        public void on(DeleteTodoItemCommand command) {
            LOGGER.info("in delete command handler, command={}", command);
            apply(new TodoItemDeletedEvent(command.todoId));
        }

        @EventHandler
        public void on(TodoItemCreatedEvent event) {
            LOGGER.info("in create event handler, event={}", event);
            id = event.todoId;
            text = event.text;
        }

        @EventHandler
        public void on(TodoItemUpdatedEvent event) {
            LOGGER.info("in update event handler, event={}", event);
            text = event.text;
        }

        @EventHandler
        public void on(TodoItemDeletedEvent event) {
            LOGGER.info("in delete event handler, event={}", event);
            // ?
        }
    }

    @Component
    public static class TodoItemUpdatingEventHandler {
        private final static Logger LOGGER = LoggerFactory.getLogger(TodoItemUpdatingEventHandler.class);

        @Autowired
        private TodoEntityRepository todoEntityRepository;

        @EventHandler
        public void handle(TodoItemCreatedEvent event) {
            LOGGER.info("in create handler, event={}", event);

            TodoEntity todoEntity = new TodoEntity();
            todoEntity.id = event.todoId;
            todoEntity.text = event.text;
            todoEntityRepository.save(todoEntity);
        }

        @EventHandler
        public void handle(TodoItemUpdatedEvent event) {
            LOGGER.info("in update handler, event={}", event);

            TodoEntity todoEntity = todoEntityRepository.findOne(event.todoId);
            todoEntity.text = event.text;
            todoEntityRepository.save(todoEntity);
        }

        @EventHandler
        public void handle(TodoItemDeletedEvent event) {
            LOGGER.info("in delete handler, event={}", event);

            todoEntityRepository.delete(event.todoId);
        }
    }

    @Component
    public static class ApiFacade {
        @Autowired
        private CommandGateway commandGateway;

        @Autowired
        private TodoEntityRepository todoEntityRepository;

        @Transactional
        public void createTodo(String id, String text) {
            commandGateway.send(new CreateTodoItemCommand(id, text));
        }

        @Transactional
        public void updateTodo(String id, String text) {
            commandGateway.send(new UpdateTodoItemCommand(id, text));
        }

        @Transactional
        public void deleteTodo(String id) {
            commandGateway.send(new DeleteTodoItemCommand(id));
        }

        @Transactional
        public long coundTodos() {
            return todoEntityRepository.count();
        }
    }
}
