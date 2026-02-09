package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.EmployeeRole;
import com.example.hrmsbackend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRoleRepo extends JpaRepository<EmployeeRole, Long> {
    @Query("SELECT er.role FROM EmployeeRole er WHERE er.employee.id = :employeeId")
    List<Role> findRoleNamesByEmployeeId(@Param("employeeId") Long employeeId);
}
