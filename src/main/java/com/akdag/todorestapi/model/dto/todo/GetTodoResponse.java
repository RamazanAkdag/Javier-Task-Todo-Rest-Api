package com.akdag.todorestapi.model.dto.todo;

import com.akdag.todorestapi.model.dto.user.GetUserResponse;
import com.akdag.todorestapi.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetTodoResponse {
    private Long id;
    private String description;
    private boolean isCompleted;
    private GetUserResponse user;
}