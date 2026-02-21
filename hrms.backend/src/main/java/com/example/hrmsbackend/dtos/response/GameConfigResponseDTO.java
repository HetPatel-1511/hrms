package com.example.hrmsbackend.dtos.response;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class GameConfigResponseDTO {
    private Long id;
    private LocalTime slotReleaseTime;
    private Integer slotDurationMinutes;
    private Integer maxPlayersPerSlot;
    private LocalTime operatingStartTime;
    private LocalTime operatingEndTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalTime getOperatingEndTime() {
        return operatingEndTime;
    }

    public void setOperatingEndTime(LocalTime operatingEndTime) {
        this.operatingEndTime = operatingEndTime;
    }

    public LocalTime getOperatingStartTime() {
        return operatingStartTime;
    }

    public void setOperatingStartTime(LocalTime operatingStartTime) {
        this.operatingStartTime = operatingStartTime;
    }

    public Integer getMaxPlayersPerSlot() {
        return maxPlayersPerSlot;
    }

    public void setMaxPlayersPerSlot(Integer maxPlayersPerSlot) {
        this.maxPlayersPerSlot = maxPlayersPerSlot;
    }

    public Integer getSlotDurationMinutes() {
        return slotDurationMinutes;
    }

    public void setSlotDurationMinutes(Integer slotDurationMinutes) {
        this.slotDurationMinutes = slotDurationMinutes;
    }

    public LocalTime getSlotReleaseTime() {
        return slotReleaseTime;
    }

    public void setSlotReleaseTime(LocalTime slotReleaseTime) {
        this.slotReleaseTime = slotReleaseTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
