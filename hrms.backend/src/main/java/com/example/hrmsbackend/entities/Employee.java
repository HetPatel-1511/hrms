package com.example.hrmsbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "employees")
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_employee_id")
    private Integer id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Email
    @Size(max = 150)
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 8)
    @Column(nullable = false)
    private String password;

    @Past
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @NotNull
    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "fk_manager_id")
    private Employee manager;

    @OneToMany(mappedBy = "manager")
    private List<Employee> subordinates;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "fk_designation_id", nullable = false)
    private Designation designation;

    @ManyToOne
    @JoinColumn(name = "fk_profile_media_id")
    private Media profileMedia;

    @OneToMany(mappedBy = "employee")
    private List<EmployeeRole> employeeRoles;

    public Integer getId() {
        return id;
    }

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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public List<Employee> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(List<Employee> subordinates) {
        this.subordinates = subordinates;
    }

    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public Media getProfileMedia() {
        return profileMedia;
    }

    public void setProfileMedia(Media profileMedia) {
        this.profileMedia = profileMedia;
    }

    public List<EmployeeRole> getEmployeeRoles() {
        return employeeRoles;
    }

    public void setEmployeeRoles(List<EmployeeRole> employeeRoles) {
        this.employeeRoles = employeeRoles;
    }
}
