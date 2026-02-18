package com.example.hrmsbackend.dtos.response;

import java.util.List;

public class MyTravelsResponseDTO {
    private EmployeeSummaryDTO employee;
    private List<TravelPlanResponseDTO> travelPlans;

    public EmployeeSummaryDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeSummaryDTO employee) {
        this.employee = employee;
    }

    public List<TravelPlanResponseDTO> getTravelPlans() {
        return travelPlans;
    }

    public void setTravelPlans(List<TravelPlanResponseDTO> travelPlans) {
        this.travelPlans = travelPlans;
    }
}
