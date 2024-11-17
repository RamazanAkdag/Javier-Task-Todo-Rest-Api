package com.akdag.todorestapi.model.dto.todo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTodoResponse {
    private Long id;
    private String description;
    private boolean isCompleted;
    private Long userId;
}
