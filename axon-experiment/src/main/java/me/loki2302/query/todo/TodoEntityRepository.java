package me.loki2302.query.todo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoEntityRepository extends JpaRepository<TodoEntity, String> {
}
