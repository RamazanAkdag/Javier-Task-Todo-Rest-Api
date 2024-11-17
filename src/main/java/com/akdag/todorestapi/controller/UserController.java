package com.akdag.todorestapi.controller;

import com.akdag.todorestapi.model.dto.user.CreateUserRequest;
import com.akdag.todorestapi.model.dto.user.CreateUserResponse;
import com.akdag.todorestapi.model.dto.user.DeleteUserResponse;
import com.akdag.todorestapi.model.dto.user.GetUserResponse;
import com.akdag.todorestapi.model.entity.User;
import com.akdag.todorestapi.service.concrete.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping
    public ResponseEntity<List<GetUserResponse>> getAll() {
        List<GetUserResponse> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getUserById(@PathVariable long id) {
        GetUserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest request) {
        CreateUserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteUserResponse> deleteUser(@PathVariable long id) {
        DeleteUserResponse response = userService.deleteUser(id);
        return ResponseEntity.ok(response);
    }
}

