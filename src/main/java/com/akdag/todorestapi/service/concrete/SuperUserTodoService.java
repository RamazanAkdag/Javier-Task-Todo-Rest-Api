package com.akdag.todorestapi.service.concrete;


import com.akdag.todorestapi.model.dto.todo.CreateTodoRequest;
import com.akdag.todorestapi.model.dto.todo.CreateTodoResponse;
import com.akdag.todorestapi.model.dto.todo.DeleteTodoResponse;
import com.akdag.todorestapi.model.dto.todo.GetTodoResponse;
import com.akdag.todorestapi.model.dto.user.GetUserResponse;
import com.akdag.todorestapi.model.entity.Todo;
import com.akdag.todorestapi.model.entity.User;
import com.akdag.todorestapi.repository.TodoRepository;
import com.akdag.todorestapi.repository.UserRepository;
import com.akdag.todorestapi.service.abstrct.ISuperUserTodoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("superUserService")
public class SuperUserTodoService implements ISuperUserTodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public SuperUserTodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<GetTodoResponse> getTodos(Long userId) {
        // Sistem içindeki tüm görevleri getir
        List<Todo> todos = todoRepository.findAll();

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
        // Kullanıcıyı doğrula
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + request.getUserId()));

        // Yeni bir görev oluştur
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
        // Güncellenecek görevi doğrula
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found with id: " + todoId));

        // Görevi güncelle
        todo.setDescription(updatedTodo.getDescription());
        todo.setCompleted(updatedTodo.isCompleted());

        return todoRepository.save(todo);
    }

    @Override
    public DeleteTodoResponse deleteTodo(Long todoId) {
        // Silinecek görevi doğrula
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found with id: " + todoId));

        todoRepository.delete(todo);

        return DeleteTodoResponse.builder()
                .id(todoId)
                .message("Todo successfully deleted")
                .build();
    }
}

