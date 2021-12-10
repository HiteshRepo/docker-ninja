package com.hiteshbootcamp.todolist.controller;

import com.hiteshbootcamp.todolist.model.TodoItem;
import com.hiteshbootcamp.todolist.repo.TodoRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// todo controller - change to trigger docker build again
@RestController
@RequestMapping(value = "/todo")
public class TodoController {

  @Autowired private TodoRepo todoRepo;

  @GetMapping
  public List<TodoItem> findAll() {
    return todoRepo.findAll();
  }

  @PostMapping
  public TodoItem save(@RequestBody TodoItem todoItem) {
    return todoRepo.save(todoItem);
  }

  @PutMapping
  public TodoItem update(@RequestBody TodoItem todoItem) {
    return todoRepo.save(todoItem);
  }

  @DeleteMapping(value = "/{id}")
  public void delete(@PathVariable Long id) {
    todoRepo.deleteById(id);
  }
}
