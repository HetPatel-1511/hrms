package com.example.hrmsbackend.services;

import com.example.hrmsbackend.dtos.request.EmployeeRequestDTO;
import com.example.hrmsbackend.dtos.response.AuthEmployeeResponseDTO;
import com.example.hrmsbackend.entities.Designation;
import com.example.hrmsbackend.entities.Employee;
import com.example.hrmsbackend.entities.EmployeeRole;
import com.example.hrmsbackend.entities.Role;
import com.example.hrmsbackend.exceptions.ResourceNotFoundException;
import com.example.hrmsbackend.mappers.EntityMapper;
import com.example.hrmsbackend.repos.DesignationRepo;
import com.example.hrmsbackend.repos.EmployeeRepo;
import com.example.hrmsbackend.repos.EmployeeRoleRepo;
import com.example.hrmsbackend.repos.RoleRepo;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {
    private JwtService jwtService;
    private EmployeeRepo employeeRepo;
    private RoleRepo roleRepo;
    private EmployeeRoleRepo employeeRoleRepo;
    private DesignationRepo designationRepo;
    private EntityMapper entityMapper;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(JwtService jwtService, EmployeeRepo employeeRepo, RoleRepo roleRepo, EmployeeRoleRepo employeeRoleRepo, DesignationRepo designationRepo, EntityMapper entityMapper, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.employeeRepo = employeeRepo;
        this.roleRepo = roleRepo;
        this.employeeRoleRepo = employeeRoleRepo;
        this.designationRepo = designationRepo;
        this.entityMapper = entityMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthEmployeeResponseDTO login(EmployeeRequestDTO employeeRequestDTO) {
        Employee employee = employeeRepo.findByEmail(employeeRequestDTO.getEmail());
        if (employee==null){
            throw new ResourceNotFoundException("User not found");
        }

        if (!passwordEncoder.matches(employeeRequestDTO.getPassword(), employee.getPassword())){
            throw new RuntimeException("Incorrect Password");
        }

        Map<String, String> tokens = generateTokens(employee);
        String accessToken = tokens.get("ACCESS_TOKEN");
        String refreshToken = tokens.get("REFRESH_TOKEN");

        AuthEmployeeResponseDTO authEmployeeResponseDTO = entityMapper.toAuthEmployeeResponseDTO(employee);
        authEmployeeResponseDTO.setAccessToken(accessToken);
        authEmployeeResponseDTO.setRefreshToken(refreshToken);

        return authEmployeeResponseDTO;
    }

    @Transactional
    public AuthEmployeeResponseDTO register(EmployeeRequestDTO employeeRequestDTO) {
        if (employeeRepo.existsByEmail(employeeRequestDTO.getEmail())) {
            throw new RuntimeException("Employee with this email already exist");
        }

        employeeRequestDTO.setPassword(passwordEncoder.encode(employeeRequestDTO.getPassword()));
        Employee employee = entityMapper.toEmployee(employeeRequestDTO);

        Designation designation = getDesignation(employeeRequestDTO);
        employee.setDesignation(designation);

        Long managerId = employeeRequestDTO.getManagerId();
        if (managerId != null) {
            Employee manager = getManager(managerId);
            employee.setManager(manager);
        }

        employeeRepo.save(employee);

        setEmployeeRoles(employeeRequestDTO.getRoleIds(), employee);

        AuthEmployeeResponseDTO authEmployeeResponseDTO = entityMapper.toAuthEmployeeResponseDTO(employee);
        authEmployeeResponseDTO.setRoles(entityMapper.toRoleResponseDTOList(employeeRoleRepo.findRoleNamesByEmployeeId(employee.getId())));

        return authEmployeeResponseDTO;
    }

    private Employee getManager(Long id) {
        Employee manager = employeeRepo.findById(id).orElse(null);
        if (manager==null) {
            throw new ResourceNotFoundException("Manager not found");
        }
        return manager;
    }

    private void setEmployeeRoles(List<Long> roleIds, Employee employee) {
        for (Long roleId: roleIds){
            Role role = roleRepo.findById(roleId).orElse(null);
            if (role==null) {
                continue;
            }
            EmployeeRole employeeRole = new EmployeeRole();
            employeeRole.setRole(role);
            employeeRole.setEmployee(employee);
            employeeRoleRepo.save(employeeRole);
        }
    }

    private Designation getDesignation(EmployeeRequestDTO employeeRequestDTO) {
        Designation designation = designationRepo.findById(employeeRequestDTO.getDesignationId()).orElse(null);
        if (designation==null) {
            throw new ResourceNotFoundException("Designation doesn't exist");
        }
        return designation;
    }

    public Map<String, String> generateTokens(Employee employee) {
        Map<String, String> tokens = new HashMap<>();

        String accessToken = jwtService.generateToken(employee, "ACCESS_TOKEN");
        String refreshToken = jwtService.generateToken(employee, "REFRESH_TOKEN");

        tokens.put("ACCESS_TOKEN", accessToken);
        tokens.put("REFRESH_TOKEN", refreshToken);

        return tokens;
    }

    public AuthEmployeeResponseDTO refreshToken(String refreshToken) {
        if (jwtService.isExpired(refreshToken)){
            throw new RuntimeException("Token is expired. Please login again");
        }

        Claims claims = jwtService.extractAllClaims(refreshToken);

        Employee employee = employeeRepo.findById(((Number) claims.get("id")).longValue()).orElse(null);
        if (employee==null){
            throw new ResourceNotFoundException("Employee doesn't exist");
        }

        Map<String, String> tokens = generateTokens(employee);
        String accessToken = tokens.get("ACCESS_TOKEN");
        String newRefreshToken = tokens.get("REFRESH_TOKEN");

        AuthEmployeeResponseDTO authEmployeeResponseDTO = entityMapper.toAuthEmployeeResponseDTO(employee);
        authEmployeeResponseDTO.setAccessToken(accessToken);
        authEmployeeResponseDTO.setRefreshToken(newRefreshToken);

        return authEmployeeResponseDTO;
    }

    public Cookie newCookie(String key, String value, Integer maxAge, Boolean httpOnly, String path) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(httpOnly);
        cookie.setPath(path);
        return cookie;
    }
}
