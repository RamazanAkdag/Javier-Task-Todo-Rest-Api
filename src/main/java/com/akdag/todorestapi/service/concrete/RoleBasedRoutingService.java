package com.akdag.todorestapi.service.concrete;

import com.akdag.todorestapi.model.Role;
import com.akdag.todorestapi.model.entity.User;
import com.akdag.todorestapi.repository.UserRepository;
import com.akdag.todorestapi.service.abstrct.ITodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoleBasedRoutingService {
    private final UserRepository userRepository;
    private final ITodoService employeeService;
    private final ITodoService companyAdminService;
    private final ITodoService superUserService;

    public RoleBasedRoutingService(
            UserRepository userRepository,
            @Qualifier("employeeService") ITodoService employeeService,
            @Qualifier("companyAdminService") ITodoService companyAdminService,
            @Qualifier("superUserService") ITodoService superUserService) {
        this.userRepository = userRepository;
        this.employeeService = employeeService;
        this.companyAdminService = companyAdminService;
        this.superUserService = superUserService;
    }

    public ITodoService getService(long requestingUserId, long targetUserId) {

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new IllegalArgumentException("Requesting user not found with id: " + requestingUserId));

        Role role = requestingUser.getRole();


        if (role == Role.EMPLOYEE && requestingUserId != targetUserId) {
            throw new SecurityException("Employees can only access their own data.");
        }


        switch (role) {
            case EMPLOYEE:
                return employeeService;
            case COMP_ADMIN:

                validateCompanyAdminAccess(targetUserId);
                return companyAdminService;
            case SUPER_USER:

                return superUserService;
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }

    private void validateCompanyAdminAccess(long targetUserId) {
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("Target user not found with id: " + targetUserId));

        if (targetUser.getRole() != Role.EMPLOYEE) {
            throw new SecurityException("Company Admins can only access Employee data.");
        }

    }
}
