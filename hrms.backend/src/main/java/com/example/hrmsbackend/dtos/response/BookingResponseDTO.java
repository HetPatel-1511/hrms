package com.example.hrmsbackend.dtos.response;

import java.time.LocalDate;
import java.util.List;

public class BookingResponseDTO {
    private LocalDate slotDate;
    private List<BookingSlotTimeDTO> slotTimes;

    public LocalDate getSlotDate() {
        return slotDate;
    }

    public void setSlotDate(LocalDate slotDate) {
        this.slotDate = slotDate;
    }

    public List<BookingSlotTimeDTO> getSlotTimes() {
        return slotTimes;
    }

    public void setSlotTimes(List<BookingSlotTimeDTO> slotTimes) {
        this.slotTimes = slotTimes;
    }
}
