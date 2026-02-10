package com.example.hrmsbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.*;
import java.util.List;

@Entity
@Table(name = "travel_plans")
public class TravelPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_travel_plan_id")
    private Long id;

    @NotBlank
    private String place;

    @NotBlank
    private String purpose;

    @NotNull
    @Column(name = "start_date")
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    @ManyToOne
    @JoinColumn(name = "fk_created_by", nullable = false)
    private Employee createdBy;

    @OneToMany(mappedBy = "travelPlan")
    private List<TravelPlanEmployee> travelPlanEmployees;

    public Long getId() {
        return id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Employee getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Employee createdBy) {
        this.createdBy = createdBy;
    }

    public List<TravelPlanEmployee> getTravelPlanEmployees() {
        return travelPlanEmployees;
    }

    public void setTravelPlanEmployees(List<TravelPlanEmployee> travelPlanEmployees) {
        this.travelPlanEmployees = travelPlanEmployees;
    }
}
