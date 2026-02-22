package com.example.hrmsbackend.dtos.response;

import java.time.LocalDate;
import java.util.List;

public class GameBookingResponseDTO {
    private GameResponseDTO game;
    private List<BookingResponseDTO> booking;

    public GameResponseDTO getGame() {
        return game;
    }

    public void setGame(GameResponseDTO game) {
        this.game = game;
    }

    public List<BookingResponseDTO> getBooking() {
        return booking;
    }

    public void setBooking(List<BookingResponseDTO> booking) {
        this.booking = booking;
    }
}
