package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignationRepo extends JpaRepository<Designation, Long> {
}
