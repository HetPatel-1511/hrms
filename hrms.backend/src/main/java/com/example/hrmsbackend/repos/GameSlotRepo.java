package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.Game;
import com.example.hrmsbackend.entities.GameSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GameSlotRepo extends JpaRepository<GameSlot, Long> {
    
    @Query("SELECT CASE WHEN COUNT(gs) > 0 THEN true ELSE false END FROM GameSlot gs WHERE gs.game.id = :gameId AND gs.slotDate = :date")
    boolean existsByGameIdAndSlotDate(@Param("gameId") Long gameId, @Param("date") LocalDate date);
    
    @Query("SELECT gs FROM GameSlot gs WHERE gs.game.id = :gameId AND gs.slotDate >= :currentDate AND gs.slotStatus IN ('AVAILABLE') ORDER BY gs.slotDate ASC, gs.startTime ASC")
    List<GameSlot> findUpcomingSlotsByGameId(@Param("gameId") Long gameId, @Param("currentDate") LocalDate currentDate);
}
