package com.example.hrmsbackend.services;

import com.example.hrmsbackend.dtos.response.AuthEmployeeResponseDTO;
import com.example.hrmsbackend.dtos.response.EmployeeDetailsResponseDTO;
import com.example.hrmsbackend.dtos.response.EmployeeSummaryDTO;
import com.example.hrmsbackend.entities.Employee;
import com.example.hrmsbackend.entities.EmployeeRole;
import com.example.hrmsbackend.entities.Role;
import com.example.hrmsbackend.exceptions.ResourceNotFoundException;
import com.example.hrmsbackend.mappers.EntityMapper;
import com.example.hrmsbackend.repos.EmployeeRepo;
import com.example.hrmsbackend.repos.EmployeeRoleRepo;
import com.example.hrmsbackend.repos.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeService {
    private EmployeeRepo employeeRepo;
    private EntityMapper entityMapper;
    private EmployeeRoleRepo employeeRoleRepo;
    private RoleRepo roleRepo;

    @Autowired
    public EmployeeService(EmployeeRepo employeeRepo, EntityMapper entityMapper, EmployeeRoleRepo employeeRoleRepo, RoleRepo roleRepo) {
        this.employeeRepo = employeeRepo;
        this.entityMapper = entityMapper;
        this.employeeRoleRepo = employeeRoleRepo;
        this.roleRepo = roleRepo;
    }

    public List<AuthEmployeeResponseDTO> getAll() {
        return entityMapper.toAuthEmployeeResponseDTOList(employeeRepo.findAll());
    }

    public AuthEmployeeResponseDTO getById(Long id) {
        Employee employee = employeeRepo.findById(id).orElse(null);
        if (employee == null) {
            throw new ResourceNotFoundException("Employe with id " + id + " doesn't exist");
        }
        return entityMapper.toAuthEmployeeResponseDTO(employee);
    }

    public EmployeeDetailsResponseDTO getEmployeeDetails(Long id) {
        Employee employee = employeeRepo.findByIdWithDetails(id);
        if (employee == null) {
            throw new ResourceNotFoundException("Employee with id " + id + " doesn't exist");
        }

        EmployeeDetailsResponseDTO response = entityMapper.toEmployeeDetailsResponseDTO(employee);

        setDirectReports(employee, response);

        return response;
    }

    private void setDirectReports(Employee employee, EmployeeDetailsResponseDTO response) {
        if (employee.getSubordinates() != null) {
            List<EmployeeSummaryDTO> directReports = entityMapper.toEmployeeSummaryDTOList(employee.getSubordinates());
            response.setDirectReports(directReports);
        }
    }

    public List<AuthEmployeeResponseDTO> getByRole(String roleName) {
        List<Employee> employees = employeeRepo.findByRoleName(roleName);
        return entityMapper.toAuthEmployeeResponseDTOList(employees);
    }

    public List<AuthEmployeeResponseDTO> searchEmployees(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll();
        }
        List<Employee> employees = employeeRepo.findByNameOrEmailContaining(searchTerm.trim());
        return entityMapper.toAuthEmployeeResponseDTOList(employees);
    }

    public String changeEmployeeRoles(Long employeeId, List<String> roleNames) {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if (employee == null) {
            throw new ResourceNotFoundException("Employee with id " + employeeId + " doesn't exist");
        }

        employeeRoleRepo.deleteByEmployeeId(employeeId);

        if (roleNames != null && !roleNames.isEmpty()) {
            for (String roleName : roleNames) {
                Role role = roleRepo.findByName(roleName);
                if (role == null) {
                    throw new ResourceNotFoundException("Role with name '" + roleName + "' doesn't exist");
                }

                EmployeeRole employeeRole = new EmployeeRole();
                employeeRole.setEmployee(employee);
                employeeRole.setRole(role);
                employeeRole.setAssignedAt(LocalDateTime.now());
                employeeRoleRepo.save(employeeRole);
            }
        }

        return "Roles updated successfully";
    }
}
