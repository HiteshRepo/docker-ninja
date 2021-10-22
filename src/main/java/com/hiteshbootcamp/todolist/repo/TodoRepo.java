package com.hiteshbootcamp.todolist.repo;

import com.hiteshbootcamp.todolist.model.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepo extends JpaRepository<TodoItem, Long> {
}
