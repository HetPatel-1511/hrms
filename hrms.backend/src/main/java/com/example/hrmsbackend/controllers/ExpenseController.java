package com.example.hrmsbackend.controllers;

import com.example.hrmsbackend.dtos.request.CustomUserDetails;
import com.example.hrmsbackend.dtos.request.ExpenseCreateRequestDTO;
import com.example.hrmsbackend.dtos.request.ExpenseStatusRequestDTO;
import com.example.hrmsbackend.dtos.response.ApiResponse;
import com.example.hrmsbackend.dtos.response.ExpenseListResponseDTO;
import com.example.hrmsbackend.dtos.response.ExpenseResponseDTO;
import com.example.hrmsbackend.services.ExpenseService;
import com.example.hrmsbackend.utils.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    private ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ExpenseResponseDTO>> create(
            @RequestPart("travelPlanEmployeeId") String travelPlanEmployeeId,
            @RequestPart("amount") String amount,
            @RequestPart("description") String description,
            @RequestPart("expenseMedia") MultipartFile expenseMedia,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ExpenseCreateRequestDTO dto = new ExpenseCreateRequestDTO();
        dto.setTravelPlanEmployeeId(Long.parseLong(travelPlanEmployeeId));
        dto.setAmount(Integer.parseInt(amount));
        dto.setDescription(description);
        dto.setExpenseMedia(expenseMedia);

        return ResponseEntity.ok(ResponseUtil.success(expenseService.create(dto, userDetails), "Expense created successfully", 201));
    }

    @PutMapping("/{expenseId}/status")
    public ResponseEntity<ApiResponse<ExpenseResponseDTO>> changeExpenseStatus(
            @PathVariable Long expenseId,
            @RequestBody ExpenseStatusRequestDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        dto.setExpenseId(expenseId);
        return ResponseEntity.ok(ResponseUtil.success(expenseService.changeExpenseStatus(dto, userDetails), "Expense status updated successfully", 200));
    }

    @GetMapping("/travel-plan-employee/{travelPlanEmployeeId}")
    public ResponseEntity<ApiResponse<ExpenseListResponseDTO>> getExpensesByTravelPlanEmployeeId(
            @PathVariable Long travelPlanEmployeeId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(ResponseUtil.success(expenseService.getExpensesByTravelPlanEmployeeId(travelPlanEmployeeId, userDetails), "Expenses fetched successfully", 200));
    }
}
