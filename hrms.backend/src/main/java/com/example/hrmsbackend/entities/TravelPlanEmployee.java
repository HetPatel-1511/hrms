package com.example.hrmsbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "travel_plan_employees")
public class TravelPlanEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_travel_plan_employee_id")
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "fk_employee_id", nullable = false)
    private Employee employee;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "fk_travel_plan_id", nullable = false)
    private TravelPlan travelPlan;

    @Positive
    private Integer maxAmountPerDay;

    public Integer getId() {
        return id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public TravelPlan getTravelPlan() {
        return travelPlan;
    }

    public void setTravelPlan(TravelPlan travelPlan) {
        this.travelPlan = travelPlan;
    }

    public Integer getMaxAmountPerDay() {
        return maxAmountPerDay;
    }

    public void setMaxAmountPerDay(Integer maxAmountPerDay) {
        this.maxAmountPerDay = maxAmountPerDay;
    }
}
