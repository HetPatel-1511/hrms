package com.example.hrmsbackend.dtos.response;

import java.time.LocalTime;
import java.util.List;

public class BookingSlotTimeDTO {
    private LocalTime startTime;
    private LocalTime endTime;
    private String bookingStatus;
    private Long slotId;
    private Long bookingId;
    private List<EmployeeSummaryDTO> playedWith;
    private EmployeeSummaryDTO bookedBy;

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

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public List<EmployeeSummaryDTO> getPlayedWith() {
        return playedWith;
    }

    public void setPlayedWith(List<EmployeeSummaryDTO> playedWith) {
        this.playedWith = playedWith;
    }

    public EmployeeSummaryDTO getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(EmployeeSummaryDTO bookedBy) {
        this.bookedBy = bookedBy;
    }
}
