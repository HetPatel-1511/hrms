package com.example.hrmsbackend.controllers;

import com.example.hrmsbackend.dtos.request.CustomUserDetails;
import com.example.hrmsbackend.dtos.request.GameBookingRequestDTO;
import com.example.hrmsbackend.dtos.request.GameRequestDTO;
import com.example.hrmsbackend.dtos.request.GameConfigUpdateDTO;
import com.example.hrmsbackend.dtos.response.ApiResponse;
import com.example.hrmsbackend.dtos.response.GameResponseDTO;
import com.example.hrmsbackend.dtos.response.GameConfigResponseDTO;
import com.example.hrmsbackend.dtos.response.EmployeeSummaryDTO;
import com.example.hrmsbackend.entities.Employee;
import com.example.hrmsbackend.services.GameService;
import com.example.hrmsbackend.utils.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/games")
public class GameController {
    private GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GameResponseDTO>>> getAll() {
        return ResponseEntity.ok(ResponseUtil.success(gameService.getAll(), "Games fetched successfully", 200));
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<ApiResponse<GameResponseDTO>> getGameById(@PathVariable Long gameId) {
        return ResponseEntity.ok(ResponseUtil.success(gameService.getGameById(gameId), "Game fetched successfully", 200));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@Valid @RequestBody GameRequestDTO gameRequestDTO) {
        return ResponseEntity.ok(ResponseUtil.success(gameService.create(gameRequestDTO), "Game creates successfully", 201));
    }

    @PostMapping("/book")
    public ResponseEntity<ApiResponse<String>> book(@Valid @RequestBody GameBookingRequestDTO gameBookingRequestDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ResponseUtil.success(gameService.book(gameBookingRequestDTO, userDetails), "Game creates successfully", 201));
    }

    @GetMapping("/{gameId}/interested")
    public ResponseEntity<ApiResponse<List<EmployeeSummaryDTO>>> getInterestedUsers(@PathVariable Long gameId) {
        return ResponseEntity.ok(ResponseUtil.success(gameService.getInterestedUsers(gameId), "Interested users fetched successfully", 200));
    }

    @PostMapping("/{gameId}/interested")
    public ResponseEntity<ApiResponse<String>> interested(@PathVariable Long gameId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ResponseUtil.success(gameService.interested(gameId, userDetails), "Interest updated successfully", 201));
    }

    @PostMapping("/slots/{slotId}/allocate")
    public ResponseEntity<ApiResponse<String>> allocateSlot(@PathVariable Long slotId) {
        return ResponseEntity.ok(ResponseUtil.success(gameService.allocateSlot(slotId), "Slot allocated successfully", 200));
    }

    @PostMapping("/bookings/{bookingId}/cancel")
    public ResponseEntity<ApiResponse<String>> cancelBooking(@PathVariable Long bookingId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ResponseUtil.success(gameService.cancelBooking(bookingId, userDetails), "Booking cancelled successfully", 200));
    }

    @PutMapping("/{gameId}/configuration")
    public ResponseEntity<ApiResponse<String>> updateGameConfiguration(
            @PathVariable Long gameId, 
            @Valid @RequestBody GameConfigUpdateDTO gameConfigUpdateDTO) {
        return ResponseEntity.ok(ResponseUtil.success(gameService.updateGameConfiguration(gameId, gameConfigUpdateDTO), "Game configuration updated successfully", 200));
    }
}
