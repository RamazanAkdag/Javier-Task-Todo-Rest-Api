package com.akdag.todorestapi.service.abstrct;

import com.akdag.todorestapi.model.dto.todo.CreateTodoRequest;
import com.akdag.todorestapi.model.dto.todo.CreateTodoResponse;
import com.akdag.todorestapi.model.dto.todo.DeleteTodoResponse;
import com.akdag.todorestapi.model.dto.todo.GetTodoResponse;
import com.akdag.todorestapi.model.entity.Todo;

import java.util.List;

public interface ITodoService {

    List<GetTodoResponse> getTodos(Long userId);

    CreateTodoResponse createTodo(CreateTodoRequest todo);

    Todo updateTodo(Long todoId, GetTodoResponse updatedTodo);

    DeleteTodoResponse deleteTodo(Long todoId);
}

