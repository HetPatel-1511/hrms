package com.example.hrmsbackend.dtos.response;

import java.time.LocalDate;
import java.util.List;

public class GameSlotGroupedResponseDTO {
    private LocalDate slotDate;
    private List<SlotTimeDTO> slotTimes;

    public LocalDate getSlotDate() {
        return slotDate;
    }

    public void setSlotDate(LocalDate slotDate) {
        this.slotDate = slotDate;
    }

    public List<SlotTimeDTO> getSlotTimes() {
        return slotTimes;
    }

    public void setSlotTimes(List<SlotTimeDTO> slotTimes) {
        this.slotTimes = slotTimes;
    }
}
