package com.example.hrmsbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "game_configurations")
public class GameConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_game_configuration_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_game_id", unique = true, nullable = false)
    private Game game;

    @NotNull
    @Column(name = "slot_release_time", nullable = false)
    private LocalTime slotReleaseTime;

    @NotNull
    @Column(name = "slot_duration_minutes", nullable = false)
    private Integer slotDurationMinutes;

    @NotNull
    @Column(name = "max_players_per_slot", nullable = false)
    private Integer maxPlayersPerSlot;

    @NotNull
    @Column(name = "operating_start_time", nullable = false)
    private LocalTime operatingStartTime;

    @NotNull
    @Column(name = "operating_end_time", nullable = false)
    private LocalTime operatingEndTime;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Integer getSlotDurationMinutes() {
        return slotDurationMinutes;
    }

    public void setSlotDurationMinutes(Integer slotDurationMinutes) {
        this.slotDurationMinutes = slotDurationMinutes;
    }

    public Integer getMaxPlayersPerSlot() {
        return maxPlayersPerSlot;
    }

    public void setMaxPlayersPerSlot(Integer maxPlayersPerSlot) {
        this.maxPlayersPerSlot = maxPlayersPerSlot;
    }

    public LocalTime getOperatingStartTime() {
        return operatingStartTime;
    }

    public void setOperatingStartTime(LocalTime operatingStartTime) {
        this.operatingStartTime = operatingStartTime;
    }

    public LocalTime getOperatingEndTime() {
        return operatingEndTime;
    }

    public void setOperatingEndTime(LocalTime operatingEndTime) {
        this.operatingEndTime = operatingEndTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalTime getSlotReleaseTime() {
        return slotReleaseTime;
    }

    public void setSlotReleaseTime(LocalTime slotReleaseTime) {
        this.slotReleaseTime = slotReleaseTime;
    }
}
