package me.loki2302.query.todocount;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoCountEntityRepository extends JpaRepository<TodoCountEntity, String> {
}
