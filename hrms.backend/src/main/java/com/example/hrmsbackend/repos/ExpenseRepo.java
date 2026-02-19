package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.Expense;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, Long> {

    @Query("SELECT e FROM Expense e WHERE e.travelPlanEmployee.id = :travelPlanEmployeeId AND e.status != :status")
    List<Expense> findByTravelPlanEmployeeIdExcludingStatus(Long travelPlanEmployeeId, String status);

    List<Expense> findByTravelPlanEmployeeId(Long travelPlanEmployeeId);
}
