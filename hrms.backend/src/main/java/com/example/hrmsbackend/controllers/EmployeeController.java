package com.example.hrmsbackend.controllers;

import com.example.hrmsbackend.dtos.response.ApiResponse;
import com.example.hrmsbackend.dtos.response.AuthEmployeeResponseDTO;
import com.example.hrmsbackend.dtos.response.EmployeeDetailsResponseDTO;
import com.example.hrmsbackend.services.EmployeeService;
import com.example.hrmsbackend.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AuthEmployeeResponseDTO>>> getAll() {
        return ResponseEntity.ok(ResponseUtil.success(employeeService.getAll(), "Employees fetched successfully", 200));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthEmployeeResponseDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseUtil.success(employeeService.getById(id), "Employees fetched successfully", 200));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<ApiResponse<EmployeeDetailsResponseDTO>> getEmployeeDetails(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseUtil.success(employeeService.getEmployeeDetails(id), "Employee details fetched successfully", 200));
    }
}
