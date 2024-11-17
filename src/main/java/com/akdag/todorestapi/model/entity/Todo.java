package com.akdag.todorestapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private boolean isCompleted;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;




}
