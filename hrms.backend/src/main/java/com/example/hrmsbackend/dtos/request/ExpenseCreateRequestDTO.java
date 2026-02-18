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

    @Positive(groups = Create.class, message = "Amount must be positive")
    private Integer amount;

    @NotBlank(groups = Create.class, message = "Description is required")
    private String description;

    @NotNull(groups = Create.class, message = "Proof file is mandatory")
    private List<MultipartFile> expenseMedias;

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
}
