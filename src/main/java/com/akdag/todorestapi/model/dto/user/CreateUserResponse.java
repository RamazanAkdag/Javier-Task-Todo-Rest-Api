package com.akdag.todorestapi.model.dto.user;

import com.akdag.todorestapi.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserResponse {
    private long id;
    private String name;
    private Role role;

}
