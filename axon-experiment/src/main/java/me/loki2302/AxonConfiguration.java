package me.loki2302;

import me.loki2302.domain.TodoAggregateRoot;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.annotation.AnnotationCommandHandlerBeanPostProcessor;
import org.axonframework.commandhandling.gateway.CommandGatewayFactoryBean;
import org.axonframework.common.jpa.ContainerManagedEntityManagerProvider;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerBeanPostProcessor;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.jpa.DomainEventEntry;
import org.axonframework.eventstore.jpa.JpaEventStore;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackageClasses = DomainEventEntry.class)
public class AxonConfiguration {
    @Bean
    public CommandBus commandBus() {
        return new SimpleCommandBus();
    }

    /*@Bean
    public CommandGatewayFactoryBean<CommandGateway> commandGatewayFactoryBean() {
        CommandGatewayFactoryBean<CommandGateway> factory = new CommandGatewayFactoryBean<>();
        factory.setCommandBus(commandBus());
        return factory;
    }*/

    /*
    Axon has very weird default behavior to interpret RuntimeExceptions as 'retryable' and
    entirely disallow checked Exceptions which were not declared on CommandGateway's send()
    level. http://www.axonframework.org/docs/2.0/command-handling.html
     */
    @Bean
    public CommandGatewayFactoryBean<AppCommandGateway> commandGatewayFactoryBean() {
        CommandGatewayFactoryBean<AppCommandGateway> factory = new CommandGatewayFactoryBean<>();
        factory.setCommandBus(commandBus());
        factory.setGatewayInterface(AppCommandGateway.class);
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
    public EventSourcingRepository<TodoAggregateRoot> todoItemRepository() {
        EventSourcingRepository<TodoAggregateRoot> repository =
                new EventSourcingRepository<>(TodoAggregateRoot.class, eventStore());
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
    public AggregateAnnotationCommandHandler<TodoAggregateRoot> taskCommandHandler() {
        return AggregateAnnotationCommandHandler.subscribe(TodoAggregateRoot.class, todoItemRepository(), commandBus());
    }
}
