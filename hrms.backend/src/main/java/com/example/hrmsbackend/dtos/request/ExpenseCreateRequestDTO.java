package com.example.hrmsbackend.dtos.request;

import com.example.hrmsbackend.dtos.Create;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ExpenseCreateRequestDTO {
    @NotNull(groups = Create.class, message = "Travel plan ID is required")
    private Long travelPlanId;

    @NotNull(groups = Create.class, message = "Employee ID is required")
    private Long employeeId;

    private Long expenseId;

    @Positive(groups = Create.class, message = "Amount must be positive")
    private Integer amount;

    @NotBlank(groups = Create.class, message = "Description is required")
    private String description;

    @NotNull(groups = Create.class, message = "Proof file is mandatory")
    private List<MultipartFile> expenseMedias;

    @NotNull(groups = Create.class, message = "Status is mandatory")
    private  String status = "Draft";

    private  List<Long> existingMediaIdsToKeep;

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

    public Long getTravelPlanId() {
        return travelPlanId;
    }

    public void setTravelPlanId(Long travelPlanId) {
        this.travelPlanId = travelPlanId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public List<MultipartFile> getExpenseMedias() {
        return expenseMedias;
    }

    public void setExpenseMedias(List<MultipartFile> expenseMedias) {
        this.expenseMedias = expenseMedias;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Long> getExistingMediaIdsToKeep() {
        return existingMediaIdsToKeep;
    }

    public void setExistingMediaIdsToKeep(List<Long> existingMediaIdsToKeep) {
        this.existingMediaIdsToKeep = existingMediaIdsToKeep;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }
}
