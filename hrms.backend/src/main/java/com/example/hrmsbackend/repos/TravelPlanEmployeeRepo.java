package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.dtos.response.EmployeeSummaryDTO;
import com.example.hrmsbackend.entities.Employee;
import com.example.hrmsbackend.entities.Role;
import com.example.hrmsbackend.entities.TravelPlan;
import com.example.hrmsbackend.entities.TravelPlanEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelPlanEmployeeRepo extends JpaRepository<TravelPlanEmployee, Long> {
    @Query("SELECT tpe.employee FROM TravelPlanEmployee tpe WHERE tpe.travelPlan.id = :travelPlanId")
    List<Employee> findTravellingEmployeesByTravelPlanId(@Param("travelPlanId") Long travelPlanId);
}
