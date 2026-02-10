package com.example.hrmsbackend.services;

import com.example.hrmsbackend.dtos.request.CustomUserDetails;
import com.example.hrmsbackend.entities.Employee;
import com.example.hrmsbackend.repos.EmployeeRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepo employeeRepo;

    @Autowired
    public CustomUserDetailsService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepo.findByEmail(email);
        if (employee == null) {
            throw new UsernameNotFoundException("User with email " + email + " not found");
        }

        return new CustomUserDetails(
                employee.getEmail(),
                employee.getPassword(),
                Collections.emptyList()
        );
    }
}

