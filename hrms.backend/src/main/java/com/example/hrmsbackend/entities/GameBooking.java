package com.example.hrmsbackend.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "game_bookings")
public class GameBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_game_bookings_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_game_slot_id", nullable = false)
    private GameSlot gameSlot;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_booked_by_id", nullable = false)
    private Employee bookedBy;

    @NotNull
    @Column(name = "booking_status", nullable = false)
    private String bookingStatus;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @OneToMany(mappedBy = "gameBooking", fetch = FetchType.LAZY)
    private List<EmployeeGameBooking> employeeGameBookings;

    public List<EmployeeGameBooking> getEmployeeGameBookings() {
        return employeeGameBookings;
    }

    public void setEmployeeGameBookings(List<EmployeeGameBooking> employeeGameBookings) {
        this.employeeGameBookings = employeeGameBookings;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public Employee getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(Employee bookedBy) {
        this.bookedBy = bookedBy;
    }

    public GameSlot getGameSlot() {
        return gameSlot;
    }

    public void setGameSlot(GameSlot gameSlot) {
        this.gameSlot = gameSlot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
