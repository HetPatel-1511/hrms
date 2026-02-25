package com.example.hrmsbackend.dtos.response;

import java.util.List;

public class UpcomingGameSlotBookingResponseDTO {
    private GameResponseDTO game;
    private List<SlotTimeDetailsDTO> slots;

    public GameResponseDTO getGame() {
        return game;
    }

    public void setGame(GameResponseDTO game) {
        this.game = game;
    }

    public List<SlotTimeDetailsDTO> getSlots() {
        return slots;
    }

    public void setSlots(List<SlotTimeDetailsDTO> slots) {
        this.slots = slots;
    }
}
