package com.example.hrmsbackend.services;

import com.example.hrmsbackend.dtos.response.AuthEmployeeResponseDTO;
import com.example.hrmsbackend.dtos.response.EmployeeDetailsResponseDTO;
import com.example.hrmsbackend.dtos.response.EmployeeSummaryDTO;
import com.example.hrmsbackend.entities.Employee;
import com.example.hrmsbackend.exceptions.ResourceNotFoundException;
import com.example.hrmsbackend.mappers.EntityMapper;
import com.example.hrmsbackend.repos.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private EmployeeRepo employeeRepo;
    private EntityMapper entityMapper;

    @Autowired
    public EmployeeService(EmployeeRepo employeeRepo, EntityMapper entityMapper) {
        this.employeeRepo = employeeRepo;
        this.entityMapper = entityMapper;
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
}
