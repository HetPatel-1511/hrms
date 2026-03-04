package com.example.hrmsbackend.dtos.request;

import java.util.List;

public class EmployeeRolesRequestDTO {
    private List<String> roles;

    public EmployeeRolesRequestDTO() {
    }

    public EmployeeRolesRequestDTO(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
