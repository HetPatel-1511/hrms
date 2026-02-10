package com.example.hrmsbackend.dtos.request;

import com.example.hrmsbackend.dtos.Login;
import com.example.hrmsbackend.dtos.Register;
import com.example.hrmsbackend.entities.Designation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class EmployeeRequestDTO {
    @NotNull(groups = {Register.class})
    @Size(max = 200)
    private String name;

    @NotNull(groups = {Login.class, Register.class}, message = "Email cannot be empty")
    @Email(groups = {Login.class, Register.class}, message = "Email is invalid")
    private String email;

    @NotNull(groups = {Login.class, Register.class})
    @Size(min = 8, message = "Password should have minimum 8 characters")
    private String password;

    @NotNull(groups = {Register.class})
    private LocalDate joiningDate;

    @NotNull(groups = {Register.class})
    private Long designationId;

    @NotNull(groups = {Register.class})
    private List<Long> roleIds;

//    @NotNull(groups = {Register.class})
    private Long managerId;

    private LocalDate birthDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Long getDesignationId() {
        return designationId;
    }

    public void setDesignationId(Long designationId) {
        this.designationId = designationId;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
