package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.Game;
import com.example.hrmsbackend.entities.GameBooking;
import com.example.hrmsbackend.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GameBookingRepo extends JpaRepository<GameBooking, Long> {

    @Query("SELECT COUNT(gb) FROM GameBooking gb WHERE gb.bookedBy = :employee AND gb.bookingStatus IN ('CONFIRMED', 'COMPLETED') AND gb.gameSlot.slotDate BETWEEN :startDate AND :endDate")
    Long countEmployeeBookingsInCycle(@Param("employee") Employee employee, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT gb FROM GameBooking gb WHERE gb.gameSlot.id = :slotId AND gb.bookingStatus = 'WAITING' ORDER BY gb.createdAt ASC")
    List<GameBooking> findWaitingBookingsForSlot(@Param("slotId") Long slotId);

    @Query("SELECT gb FROM GameBooking gb WHERE gb.bookedBy = :employee AND gb.gameSlot.slotDate = :date AND gb.bookingStatus IN ('WAITING', 'CONFIRMED')")
    List<GameBooking> findEmployeeBookingsForDate(@Param("employee") Employee employee, @Param("date") LocalDate date);
}
