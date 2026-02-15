package com.example.hrmsbackend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ExpenseStatusRequestDTO {
    @NotNull(message = "Expense ID is required")
    private Long expenseId;

    @NotNull(message = "Status is required")
    private String status;

    @NotBlank(message = "Remarks are required for rejection")
    private String remarks;

    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
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
}
