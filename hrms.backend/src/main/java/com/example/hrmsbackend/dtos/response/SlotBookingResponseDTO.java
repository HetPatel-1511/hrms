package com.example.hrmsbackend.dtos.response;

import com.example.hrmsbackend.entities.Employee;

import java.time.LocalDateTime;
import java.util.List;

public class SlotBookingResponseDTO {
    private EmployeeSummaryDTO bookedBy;
    private String bookingStatus;
    private Long bookingId;
    private LocalDateTime createdAt;
    private LocalDateTime cancelledAt;
    private List<EmployeeSummaryDTO> playedWith;

    public List<EmployeeSummaryDTO> getPlayedWith() {
        return playedWith;
    }

    public void setPlayedWith(List<EmployeeSummaryDTO> playedWith) {
        this.playedWith = playedWith;
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

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public EmployeeSummaryDTO getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(EmployeeSummaryDTO bookedBy) {
        this.bookedBy = bookedBy;
    }
}
