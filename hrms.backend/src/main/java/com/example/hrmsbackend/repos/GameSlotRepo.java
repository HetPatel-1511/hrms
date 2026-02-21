package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.Game;
import com.example.hrmsbackend.entities.GameSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameSlotRepo extends JpaRepository<GameSlot, Long> {
}
