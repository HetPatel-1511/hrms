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

    @Query("SELECT r.name FROM Employee e JOIN e.employeeRoles er JOIN er.role r WHERE e.id = :employeeId")
    List<String> findRolesByEmployeeId(@Param("employeeId") Long employeeId);

    @Query("SELECT e FROM Employee e WHERE e.id = :employeeId")
    Employee findByIdWithDetails(@Param("employeeId") Long employeeId);

    @Query("SELECT e FROM Employee e JOIN e.employeeRoles er JOIN er.role r WHERE r.name = :roleName")
    List<Employee> findByRoleName(@Param("roleName") String roleName);

    @Query("SELECT e FROM Employee e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(e.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Employee> findByNameOrEmailContaining(@Param("searchTerm") String searchTerm);

    @Query(value = "SELECT * FROM employees e WHERE (e.birth_date IS NOT NULL AND MONTH(e.birth_date) = :month AND DAY(e.birth_date) = :day) OR (e.joining_date IS NOT NULL AND MONTH(e.joining_date) = :month AND DAY(e.joining_date) = :day)", nativeQuery = true)
    List<Employee> findByBirthOrJoiningMonthAndDay(@Param("month") int month, @Param("day") int day);
}
