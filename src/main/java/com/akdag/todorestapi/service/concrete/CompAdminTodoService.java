package com.akdag.todorestapi.service.concrete;

import com.akdag.todorestapi.model.Role;
import com.akdag.todorestapi.model.dto.todo.CreateTodoRequest;
import com.akdag.todorestapi.model.dto.todo.CreateTodoResponse;
import com.akdag.todorestapi.model.dto.todo.DeleteTodoResponse;
import com.akdag.todorestapi.model.dto.todo.GetTodoResponse;
import com.akdag.todorestapi.model.dto.user.GetUserResponse;
import com.akdag.todorestapi.model.entity.Todo;
import com.akdag.todorestapi.model.entity.User;
import com.akdag.todorestapi.repository.TodoRepository;
import com.akdag.todorestapi.repository.UserRepository;
import com.akdag.todorestapi.service.abstrct.ICompAdminTodoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("companyAdminService")
public class CompAdminTodoService implements ICompAdminTodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public CompAdminTodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<GetTodoResponse> getTodos(Long adminId) {
        List<Todo> todos = todoRepository.findAll().stream()
                .filter(todo -> todo.getUser().getRole() == Role.EMPLOYEE)
                .toList();

        return todos.stream()
                .map(todo -> GetTodoResponse.builder()
                        .id(todo.getId())
                        .description(todo.getDescription())
                        .isCompleted(todo.isCompleted())
                        .user(GetUserResponse.builder()
                                .id(todo.getUser().getId())
                                .name(todo.getUser().getName())
                                .role(todo.getUser().getRole())
                                .build())
                        .build())
                .toList();
    }

    @Override
    public CreateTodoResponse createTodo(CreateTodoRequest request) {
        // Görev oluşturulacak kullanıcı Employee rolünde olmalı
        User user = userRepository.findById(request.getUserId())
                .filter(u -> u.getRole() == Role.EMPLOYEE)
                .orElseThrow(() -> new IllegalArgumentException("User not found or is not an Employee with id: " + request.getUserId()));

        Todo newTodo = Todo.builder()
                .description(request.getDescription())
                .isCompleted(request.isCompleted())
                .user(user)
                .build();

        Todo savedTodo = todoRepository.save(newTodo);

        return CreateTodoResponse.builder()
                .id(savedTodo.getId())
                .description(savedTodo.getDescription())
                .isCompleted(savedTodo.isCompleted())
                .userId(savedTodo.getUser().getId())
                .build();
    }

    @Override
    public Todo updateTodo(Long todoId, GetTodoResponse updatedTodo) {

        Todo todo = todoRepository.findById(todoId)
                .filter(t -> t.getUser().getRole() == Role.EMPLOYEE)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found or does not belong to an Employee with id: " + todoId));

        todo.setDescription(updatedTodo.getDescription());
        todo.setCompleted(updatedTodo.isCompleted());

        return todoRepository.save(todo);
    }

    @Override
    public DeleteTodoResponse deleteTodo(Long todoId) {

        Todo todo = todoRepository.findById(todoId)
                .filter(t -> t.getUser().getRole() == Role.EMPLOYEE)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found or does not belong to an Employee with id: " + todoId));

        todoRepository.delete(todo);

        return DeleteTodoResponse.builder()
                .id(todoId)
                .message("Todo successfully deleted")
                .build();
    }
}

