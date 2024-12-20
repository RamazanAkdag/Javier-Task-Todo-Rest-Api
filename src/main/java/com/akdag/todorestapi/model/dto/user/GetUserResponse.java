package com.akdag.todorestapi.model.dto.user;

import com.akdag.todorestapi.model.Role;
import com.akdag.todorestapi.model.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserResponse {
    private long id;
    private String name;
    private Role role;

}
