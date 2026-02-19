package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.Expense;
import com.example.hrmsbackend.entities.ExpenseMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseMediaRepo extends JpaRepository<ExpenseMedia, Long> {
    List<ExpenseMedia> findByExpense(Expense expense);
}
