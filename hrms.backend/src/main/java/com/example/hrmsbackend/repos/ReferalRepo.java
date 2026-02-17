package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.Referal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferalRepo extends JpaRepository<Referal, Long> {
}
