package com.example.hrmsbackend.dtos.request;

import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.LocalTime;

public class GameRequestDTO {
    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Duration before slot opens is required")
    private Duration durationBeforeSlotOpens;

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

    public Duration getDurationBeforeSlotOpens() {
        return durationBeforeSlotOpens;
    }

    public void setDurationBeforeSlotOpens(Duration durationBeforeSlotOpens) {
        this.durationBeforeSlotOpens = durationBeforeSlotOpens;
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
