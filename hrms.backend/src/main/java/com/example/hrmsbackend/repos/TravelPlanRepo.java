package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.TravelPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelPlanRepo extends JpaRepository<TravelPlan, Long> {
    List<TravelPlan> findAllByOrderByCreatedAtDesc();
}
