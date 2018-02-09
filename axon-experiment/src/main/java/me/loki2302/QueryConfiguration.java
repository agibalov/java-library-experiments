package me.loki2302;

import me.loki2302.query.todo.TodoEntity;
import me.loki2302.query.todocount.TodoCountEntity;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackageClasses = {
        TodoEntity.class,
        TodoCountEntity.class
})
public class QueryConfiguration {
}
