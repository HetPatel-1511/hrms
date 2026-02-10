package com.example.hrmsbackend.controllers;

import com.example.hrmsbackend.dtos.Create;
import com.example.hrmsbackend.dtos.Update;
import com.example.hrmsbackend.dtos.request.CustomUserDetails;
import com.example.hrmsbackend.dtos.request.TravelPlanRequestDTO;
import com.example.hrmsbackend.dtos.response.TravelPlanResponseDTO;
import com.example.hrmsbackend.services.TravelPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/travel-plans")
public class TravelPlanController {
    private TravelPlanService travelPlanService;

    @Autowired
    public TravelPlanController(TravelPlanService travelPlanService) {
        this.travelPlanService = travelPlanService;
    }

    @GetMapping
    public List<TravelPlanResponseDTO> getAll() {
        return travelPlanService.getAll();
    }

    @PostMapping
    public TravelPlanResponseDTO create(@RequestBody @Validated(Create.class) TravelPlanRequestDTO dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return travelPlanService.create(dto, userDetails);
    }

    @GetMapping("/{id}")
    public TravelPlanResponseDTO getById(@PathVariable Long id) {
        return travelPlanService.getById(id);
    }

    @PutMapping("/{id}")
    public TravelPlanResponseDTO update(@PathVariable Long id, @RequestBody @Validated(Update.class) TravelPlanRequestDTO dto) {
        return travelPlanService.update(id, dto);
    }

}
