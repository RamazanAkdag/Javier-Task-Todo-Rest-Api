package com.akdag.todorestapi.repository;

import com.akdag.todorestapi.model.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo,Long> {
    List<Todo> findByUserId(Long userId);
}
