package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.Employee;
import com.example.hrmsbackend.entities.TravelPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelPlanRepo extends JpaRepository<TravelPlan, Long> {
    List<TravelPlan> findAllByOrderByCreatedAtDesc();

    @Query("SELECT DISTINCT tp FROM TravelPlan tp " +
           "JOIN tp.travelPlanEmployees tpe " +
           "WHERE tpe.employee = :employee " +
           "ORDER BY tp.startDate DESC")
    List<TravelPlan> findByEmployeeOrderByStartDateDesc(@Param("employee") Employee employee);
}
