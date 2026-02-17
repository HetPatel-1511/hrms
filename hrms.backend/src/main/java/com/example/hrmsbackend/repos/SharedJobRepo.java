package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.SharedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedJobRepo extends JpaRepository<SharedJob, Long> {
}
