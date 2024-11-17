package com.akdag.todorestapi.repository;

import com.akdag.todorestapi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
