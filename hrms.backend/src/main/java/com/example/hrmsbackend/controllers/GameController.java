package com.example.hrmsbackend.controllers;

import com.example.hrmsbackend.dtos.request.CustomUserDetails;
import com.example.hrmsbackend.dtos.request.GameBookingRequestDTO;
import com.example.hrmsbackend.dtos.request.GameRequestDTO;
import com.example.hrmsbackend.dtos.response.ApiResponse;
import com.example.hrmsbackend.dtos.response.GameResponseDTO;
import com.example.hrmsbackend.services.GameService;
import com.example.hrmsbackend.utils.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@Valid @RequestBody GameRequestDTO gameRequestDTO) {
        return ResponseEntity.ok(ResponseUtil.success(gameService.create(gameRequestDTO), "Game creates successfully", 201));
    }

    @PostMapping("/book")
    public ResponseEntity<ApiResponse<String>> book(@Valid @RequestBody GameBookingRequestDTO gameBookingRequestDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ResponseUtil.success(gameService.book(gameBookingRequestDTO, userDetails), "Game creates successfully", 201));
    }

    @PostMapping("/{gameId}/interested")
    public ResponseEntity<ApiResponse<String>> interested(@PathVariable Long gameId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ResponseUtil.success(gameService.interested(gameId, userDetails), "Interest updated successfully", 201));
    }
}
