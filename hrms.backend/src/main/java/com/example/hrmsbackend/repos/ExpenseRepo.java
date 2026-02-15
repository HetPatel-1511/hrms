package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, Long> {
    List<Expense> findByTravelPlanEmployeeId(Long travelPlanEmployeeId);
}
