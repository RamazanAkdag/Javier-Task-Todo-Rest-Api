package com.akdag.todorestapi.service.concrete;

import com.akdag.todorestapi.model.dto.user.CreateUserRequest;
import com.akdag.todorestapi.model.dto.user.CreateUserResponse;
import com.akdag.todorestapi.model.dto.user.DeleteUserResponse;
import com.akdag.todorestapi.model.dto.user.GetUserResponse;
import com.akdag.todorestapi.model.entity.User;
import com.akdag.todorestapi.repository.UserRepository;
import com.akdag.todorestapi.service.abstrct.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    @Override
    public CreateUserResponse createUser(CreateUserRequest request) {

        User user = User.builder()
                .name(request.getName())
                .role(request.getRole())
                .todos(new ArrayList<>())
                .build();

        User saved = userRepository.save(user);

        return CreateUserResponse.builder().id(saved.getId()).name(saved.getName()).role(saved.getRole()).build();
    }

    @Override
    public DeleteUserResponse deleteUser(long id) {
        userRepository.deleteById(id);
        return DeleteUserResponse.builder().id(id).message("user deleted successfully").build();
    }

    @Override
    public GetUserResponse getUserById(long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));


        return GetUserResponse.builder().id(user.getId()).name(user.getName()).role(user.getRole()).build();
    }

    @Override
    public List<GetUserResponse> getAll() {
        List<User> users = userRepository.findAll();


        List<GetUserResponse> response = users.stream()
                .map(user ->
                        GetUserResponse.builder().id(user.getId()).name(user.getName()).role(user.getRole()).build()
                )
                .toList();

        return response;
    }
}
