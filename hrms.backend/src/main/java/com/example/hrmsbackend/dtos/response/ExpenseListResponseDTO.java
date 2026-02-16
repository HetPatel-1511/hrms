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
    private Integer totalAmount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long totalCount;

    private TravelPlanSummaryResponseDTO travelPlan;

    private EmployeeSummaryDTO employee;

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

    public TravelPlanSummaryResponseDTO getTravelPlan() {
        return travelPlan;
    }

    public void setTravelPlan(TravelPlanSummaryResponseDTO travelPlan) {
        this.travelPlan = travelPlan;
    }

    public EmployeeSummaryDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeSummaryDTO employee) {
        this.employee = employee;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }
}
