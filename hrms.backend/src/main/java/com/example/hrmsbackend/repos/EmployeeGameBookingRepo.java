package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.EmployeeGameBooking;
import com.example.hrmsbackend.entities.GameBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeGameBookingRepo extends JpaRepository<EmployeeGameBooking, Long> {
}
