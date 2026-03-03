package com.example.hrmsbackend.dtos.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SlotTimeDetailsDTO {
    private LocalTime startTime;
    private LocalTime endTime;
    private String slotStatus;
    private LocalDate slotDate;
    private Long slotId;
    private List<SlotBookingResponseDTO> bookings;

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getSlotStatus() {
        return slotStatus;
    }

    public void setSlotStatus(String slotStatus) {
        this.slotStatus = slotStatus;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public List<SlotBookingResponseDTO> getBookings() {
        return bookings;
    }

    public void setBookings(List<SlotBookingResponseDTO> bookings) {
        this.bookings = bookings;
    }

    public LocalDate getSlotDate() {
        return slotDate;
    }

    public void setSlotDate(LocalDate slotDate) {
        this.slotDate = slotDate;
    }
}
