package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.Employee;
import com.example.hrmsbackend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    boolean existsByName(String name);
    boolean existsByEmail(String email);
    Employee findByName(String name);
    Employee findByEmail(String email);
}
