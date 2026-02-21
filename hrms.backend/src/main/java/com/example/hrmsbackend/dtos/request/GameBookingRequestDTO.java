package com.example.hrmsbackend.dtos.request;

import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

public class GameBookingRequestDTO {
    @NotNull(message = "Game slot is required")
    private Long gameSlotId;

    private List<Long> employeeIds;

    public Long getGameSlotId() {
        return gameSlotId;
    }

    public void setGameSlotId(Long gameSlotId) {
        this.gameSlotId = gameSlotId;
    }

    public List<Long> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(List<Long> employeeIds) {
        this.employeeIds = employeeIds;
    }
}
