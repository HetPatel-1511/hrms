package com.example.hrmsbackend.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class ExpenseListResponseDTO {
    private List<ExpenseResponseDTO> expenses;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer totalClaimedAmount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer totalUnclaimableAmount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long totalCount;

    public List<ExpenseResponseDTO> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseResponseDTO> expenses) {
        this.expenses = expenses;
    }

    public Integer getTotalClaimedAmount() {
        return totalClaimedAmount;
    }

    public void setTotalClaimedAmount(Integer totalClaimedAmount) {
        this.totalClaimedAmount = totalClaimedAmount;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getTotalUnclaimableAmount() {
        return totalUnclaimableAmount;
    }

    public void setTotalUnclaimableAmount(Integer totalUnclaimableAmount) {
        this.totalUnclaimableAmount = totalUnclaimableAmount;
    }
}
