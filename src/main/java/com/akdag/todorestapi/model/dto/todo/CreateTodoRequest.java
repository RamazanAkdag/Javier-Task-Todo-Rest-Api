package com.akdag.todorestapi.model.dto.todo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTodoRequest {
    private String description;
    private boolean isCompleted;
    private Long userId; // Görevi kimin için oluşturacağınızı belirtir
}

