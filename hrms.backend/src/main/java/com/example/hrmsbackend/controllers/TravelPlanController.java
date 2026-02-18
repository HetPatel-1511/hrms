package com.example.hrmsbackend.controllers;

import com.example.hrmsbackend.dtos.Create;
import com.example.hrmsbackend.dtos.Update;
import com.example.hrmsbackend.dtos.request.CustomUserDetails;
import com.example.hrmsbackend.dtos.request.TravelPlanRequestDTO;
import com.example.hrmsbackend.dtos.response.ApiResponse;
import com.example.hrmsbackend.dtos.response.MyTravelsResponseDTO;
import com.example.hrmsbackend.dtos.response.TravelDocumentListResponseDTO;
import com.example.hrmsbackend.dtos.response.TravelDocumentResponseDTO;
import com.example.hrmsbackend.dtos.response.TravelPlanResponseDTO;
import com.example.hrmsbackend.services.TravelPlanService;
import com.example.hrmsbackend.utils.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<ApiResponse<List<TravelPlanResponseDTO>>> getAll() {
        return ResponseEntity.ok(ResponseUtil.success(travelPlanService.getAll(), "All travel plans fetched successfully", 200));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TravelPlanResponseDTO>> create(@RequestBody @Validated(Create.class) TravelPlanRequestDTO dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ResponseUtil.success(travelPlanService.create(dto, userDetails), "Travel plan created successfully", 201));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TravelPlanResponseDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseUtil.success(travelPlanService.getById(id), "Travel plans with id " + id + " fetched successfully", 200));
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<ApiResponse<TravelPlanResponseDTO>> update(@PathVariable Long id, @RequestBody @Validated(Update.class) TravelPlanRequestDTO dto) {
//        return ResponseEntity.ok(ResponseUtil.success(travelPlanService.update(id, dto), "Tokens refreshed successfully", 200));
//    }

    @GetMapping(value = "/{travelPlanId}/employee/{employeeId}/documents")
    public ResponseEntity<ApiResponse<TravelDocumentListResponseDTO>> getDocuments(
            @PathVariable("travelPlanId") Long travelPlanId,
            @PathVariable("employeeId") Long employeeId
    ) {
        return ResponseEntity.ok(ResponseUtil.success(travelPlanService.getDocuments(travelPlanId, employeeId), "Document fetched successfully", 200));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MyTravelsResponseDTO>> getMyTravels(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ResponseUtil.success(travelPlanService.getMyTravels(userDetails), "My travels fetched successfully", 200));
    }

    @PostMapping(value = "/{travelPlanId}/employee/{employeeId}/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<TravelDocumentResponseDTO>> uploadDocument(
            @PathVariable("travelPlanId") Long travelPlanId,
            @PathVariable("employeeId") Long employeeId,
            @RequestPart("documentTypeId") String documentTypeId,
            @RequestPart("ownerType") String ownerType,
            @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(ResponseUtil.success(travelPlanService.uploadDocument(travelPlanId, employeeId, Long.parseLong(documentTypeId), file, ownerType, userDetails), "Document uploaded successfully", 201));
    }
}
