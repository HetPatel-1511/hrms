package com.example.hrmsbackend.dtos.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public class GameRequestDTO {
    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Slot release time is required")
    private LocalTime slotReleaseTime;

    @NotNull(message = "Slot duration minutes is required")
    private Integer slotDurationMinutes;

    @NotNull(message = "Max players per slot is required")
    private Integer maxPlayersPerSlot;

    @NotNull(message = "Operating start time is required")
    private LocalTime operatingStartTime;

    @NotNull(message = "Operating end time is required")
    private LocalTime operatingEndTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getSlotReleaseTime() {
        return slotReleaseTime;
    }

    public void setSlotReleaseTime(LocalTime slotReleaseTime) {
        this.slotReleaseTime = slotReleaseTime;
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
}
