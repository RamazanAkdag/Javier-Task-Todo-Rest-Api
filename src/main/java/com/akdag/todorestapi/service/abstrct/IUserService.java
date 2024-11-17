package com.akdag.todorestapi.service.abstrct;

import com.akdag.todorestapi.model.dto.user.CreateUserRequest;
import com.akdag.todorestapi.model.dto.user.CreateUserResponse;
import com.akdag.todorestapi.model.dto.user.DeleteUserResponse;
import com.akdag.todorestapi.model.dto.user.GetUserResponse;

import java.util.List;

public interface IUserService {
    public CreateUserResponse createUser(CreateUserRequest request);
    public DeleteUserResponse deleteUser(long id);

    public GetUserResponse getUserById(long id);

    public List<GetUserResponse> getAll();




}
