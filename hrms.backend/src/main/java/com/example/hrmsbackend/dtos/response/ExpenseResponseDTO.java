package com.example.hrmsbackend.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

public class ExpenseResponseDTO {
    private Long id;

    private Integer amount;

    private String description;

    private String status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String remarks;

    private LocalDateTime createdAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime approvedAt;

    private List<MediaResponseDTO> expenseMedias;

    private EmployeeSummaryDTO employee;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private EmployeeSummaryDTO statusChangedBy;

    private TravelPlanSummaryResponseDTO travelPlan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public EmployeeSummaryDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeSummaryDTO employee) {
        this.employee = employee;
    }

    public EmployeeSummaryDTO getStatusChangedBy() {
        return statusChangedBy;
    }

    public void setStatusChangedBy(EmployeeSummaryDTO statusChangedBy) {
        this.statusChangedBy = statusChangedBy;
    }

    public TravelPlanSummaryResponseDTO getTravelPlan() {
        return travelPlan;
    }

    public void setTravelPlan(TravelPlanSummaryResponseDTO travelPlan) {
        this.travelPlan = travelPlan;
    }

    public List<MediaResponseDTO> getExpenseMedias() {
        return expenseMedias;
    }

    public void setExpenseMedias(List<MediaResponseDTO> expenseMedias) {
        this.expenseMedias = expenseMedias;
    }
}
