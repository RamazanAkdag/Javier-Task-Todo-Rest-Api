package com.akdag.todorestapi;

import com.akdag.todorestapi.model.Role;
import com.akdag.todorestapi.model.dto.todo.CreateTodoRequest;
import com.akdag.todorestapi.model.dto.todo.GetTodoResponse;
import com.akdag.todorestapi.model.dto.user.GetUserResponse;
import com.akdag.todorestapi.model.entity.Todo;
import com.akdag.todorestapi.model.entity.User;
import com.akdag.todorestapi.repository.TodoRepository;
import com.akdag.todorestapi.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class TodoRestApiApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private User employee;
    private User companyAdmin;
    private User superUser;

    private Todo employeeTodo;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Veritabanını temizle
        todoRepository.deleteAll();
        userRepository.deleteAll();

        // Kullanıcılar oluştur
        employee = User.builder()
                .name("Employee User")
                .role(Role.EMPLOYEE)
                .build();
        userRepository.save(employee);

        companyAdmin = User.builder()
                .name("Company Admin User")
                .role(Role.COMP_ADMIN)
                .build();
        userRepository.save(companyAdmin);

        superUser = User.builder()
                .name("Super User")
                .role(Role.SUPER_USER)
                .build();
        userRepository.save(superUser);

        // Employee için bir görev oluştur
        employeeTodo = Todo.builder()
                .description("Employee Task")
                .isCompleted(false)
                .user(employee)
                .build();
        todoRepository.save(employeeTodo);
    }

    // EMPLOYEE testleri
    @Test
    void testEmployeeCanAccessOwnTodos() throws Exception {
        mockMvc.perform(get("/api/todos/" + employee.getId())
                        .header("requestingUserId", employee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1)) // Kendi görevine erişiyor
                .andExpect(jsonPath("$[0].description").value("Employee Task"))
                .andExpect(jsonPath("$[0].user.id").value(employee.getId()));
    }

    @Test
    void testEmployeeCannotAccessOtherTodos() throws Exception {
        mockMvc.perform(get("/api/todos/" + companyAdmin.getId())
                        .header("requestingUserId", employee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Employees can only access their own data."));
    }

    // COMPANY_ADMIN testleri
    @Test
    void testCompanyAdminCanAccessEmployeeTodos() throws Exception {
        mockMvc.perform(get("/api/todos/" + employee.getId())
                        .header("requestingUserId", companyAdmin.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].description").value("Employee Task"))
                .andExpect(jsonPath("$[0].user.id").value(employee.getId()));
    }

    @Test
    void testCompanyAdminCannotAccessOtherAdminsTodos() throws Exception {
        mockMvc.perform(get("/api/todos/" + companyAdmin.getId())
                        .header("requestingUserId", companyAdmin.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Company Admins can only access Employee data."));
    }

    // SUPER_USER testleri
    @Test
    void testSuperUserCanAccessAllTodos() throws Exception {
        mockMvc.perform(get("/api/todos/" + employee.getId())
                        .header("requestingUserId", superUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].description").value("Employee Task"))
                .andExpect(jsonPath("$[0].user.id").value(employee.getId()));
    }

    @Test
    void testSuperUserCanAccessCompanyAdminTodos() throws Exception {
        Todo companyAdminTodo = Todo.builder()
                .description("Company Admin Task")
                .isCompleted(true)
                .user(companyAdmin)
                .build();
        todoRepository.save(companyAdminTodo);

        mockMvc.perform(get("/api/todos/" + companyAdmin.getId())
                        .header("requestingUserId", superUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

    }
}
