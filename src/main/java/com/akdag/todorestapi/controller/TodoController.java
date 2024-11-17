package com.akdag.todorestapi.controller;

import com.akdag.todorestapi.model.dto.todo.CreateTodoRequest;
import com.akdag.todorestapi.model.dto.todo.CreateTodoResponse;
import com.akdag.todorestapi.model.dto.todo.DeleteTodoResponse;
import com.akdag.todorestapi.model.dto.todo.GetTodoResponse;
import com.akdag.todorestapi.service.concrete.RoleBasedRoutingService;
import com.akdag.todorestapi.service.abstrct.ITodoService;
import com.akdag.todorestapi.repository.TodoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final RoleBasedRoutingService roleBasedRoutingService;
    private final TodoRepository todoRepository;

    public TodoController(RoleBasedRoutingService roleBasedRoutingService, TodoRepository todoRepository) {
        this.roleBasedRoutingService = roleBasedRoutingService;
        this.todoRepository = todoRepository;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<GetTodoResponse>> getTodos(@RequestHeader("requestingUserId") long requestingUserId,
                                                          @PathVariable long userId) {
        ITodoService todoService = roleBasedRoutingService.getService(requestingUserId, userId);
        List<GetTodoResponse> todos = todoService.getTodos(userId);
        return ResponseEntity.ok(todos);
    }

    @PostMapping
    public ResponseEntity<CreateTodoResponse> createTodo(@RequestHeader("requestingUserId") long requestingUserId,
                                                         @RequestBody CreateTodoRequest request) {
        ITodoService todoService = roleBasedRoutingService.getService(requestingUserId, request.getUserId());
        CreateTodoResponse createdTodo = todoService.createTodo(request);
        return ResponseEntity.ok(createdTodo);
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<Void> updateTodo(@RequestHeader("requestingUserId") long requestingUserId,
                                           @PathVariable long todoId,
                                           @RequestBody GetTodoResponse updatedTodo) {
        ITodoService todoService = roleBasedRoutingService.getService(requestingUserId, updatedTodo.getUser().getId());
        todoService.updateTodo(todoId, updatedTodo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<DeleteTodoResponse> deleteTodo(@RequestHeader("requestingUserId") long requestingUserId,
                                                         @PathVariable long todoId) {
        // Silinmek istenen görevin hedef kullanıcısının ID'sini belirle
        long targetUserId = getTargetUserIdByTodoId(todoId);
        ITodoService todoService = roleBasedRoutingService.getService(requestingUserId, targetUserId);
        DeleteTodoResponse response = todoService.deleteTodo(todoId);
        return ResponseEntity.ok(response);
    }

    private long getTargetUserIdByTodoId(long todoId) {
        return todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found with id: " + todoId))
                .getUser()
                .getId();
    }
}
