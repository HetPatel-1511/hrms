package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.Game;
import com.example.hrmsbackend.entities.GameBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameBookingRepo extends JpaRepository<GameBooking, Long> {
}
