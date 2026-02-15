package com.example.hrmsbackend.dtos.request;

import com.example.hrmsbackend.dtos.Create;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.multipart.MultipartFile;

public class ExpenseCreateRequestDTO {
    @NotNull(groups = Create.class, message = "Travel plan employee ID is required")
    private Long travelPlanEmployeeId;

    @Positive(groups = Create.class, message = "Amount must be positive")
    private Integer amount;

    @NotBlank(groups = Create.class, message = "Description is required")
    private String description;

    @NotNull(groups = Create.class, message = "Proof file is mandatory")
    private MultipartFile expenseMedia;

    public Long getTravelPlanEmployeeId() {
        return travelPlanEmployeeId;
    }

    public void setTravelPlanEmployeeId(Long travelPlanEmployeeId) {
        this.travelPlanEmployeeId = travelPlanEmployeeId;
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

    public MultipartFile getExpenseMedia() {
        return expenseMedia;
    }

    public void setExpenseMedia(MultipartFile expenseMedia) {
        this.expenseMedia = expenseMedia;
    }
}
