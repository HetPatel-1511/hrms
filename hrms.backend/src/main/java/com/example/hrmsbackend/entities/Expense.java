package com.example.hrmsbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_expense_id")
    private Long id;

    @NotNull
    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpenseMedia> expenseMedias = new ArrayList<>();

    @Positive
    private Integer amount;

    @NotBlank
    @Size(max=2000)
    private String description;

    @NotBlank
    private String status;

    private String remarks;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "fk_travel_plan_employee_id", nullable = false)
    private TravelPlanEmployee travelPlanEmployee;

    @ManyToOne
    @JoinColumn(name = "fk_status_changed_by")
    private Employee statusChangedBy;

    public Long getId() {
        return id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public TravelPlanEmployee getTravelPlanEmployee() {
        return travelPlanEmployee;
    }

    public void setTravelPlanEmployee(TravelPlanEmployee travelPlanEmployee) {
        this.travelPlanEmployee = travelPlanEmployee;
    }

    public Employee getStatusChangedBy() {
        return statusChangedBy;
    }

    public void setStatusChangedBy(Employee statusChangedBy) {
        this.statusChangedBy = statusChangedBy;
    }

    public List<ExpenseMedia> getExpenseMedias() {
        return expenseMedias;
    }

    public void setExpenseMedias(List<ExpenseMedia> expenseMedias) {
        this.expenseMedias = expenseMedias;
    }

    public void addExpenseMedia(ExpenseMedia expenseMedia) {
        expenseMedias.add(expenseMedia);
        expenseMedia.setExpense(this);
    }
}
