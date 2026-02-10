package com.example.hrmsbackend.dtos.request;

import com.example.hrmsbackend.dtos.Create;
import com.example.hrmsbackend.dtos.Update;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class TravelPlanRequestDTO {
    @NotNull(groups = {Create.class, Update.class})
    @Size(max = 200, groups = {Create.class, Update.class})
    private String place;

    @NotNull(groups = {Create.class, Update.class})
    private String purpose;

    @NotNull(groups = {Create.class, Update.class})
    private LocalDate startDate;

    @NotNull(groups = {Create.class, Update.class})
    private LocalDate endDate;

    private List<TravelingEmployeeRequestDTO> employees = List.of();

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<TravelingEmployeeRequestDTO> getEmployees() {
        return employees;
    }

    public void setEmployees(List<TravelingEmployeeRequestDTO> employees) {
        this.employees = employees;
    }
}

