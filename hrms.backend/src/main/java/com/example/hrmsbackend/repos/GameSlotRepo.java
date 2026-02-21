package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.Game;
import com.example.hrmsbackend.entities.GameSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface GameSlotRepo extends JpaRepository<GameSlot, Long> {
    
    @Query("SELECT CASE WHEN COUNT(gs) > 0 THEN true ELSE false END FROM GameSlot gs WHERE gs.game.id = :gameId AND gs.slotDate = :date")
    boolean existsByGameIdAndSlotDate(@Param("gameId") Long gameId, @Param("date") LocalDate date);
}
