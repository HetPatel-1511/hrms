package com.example.hrmsbackend.controllers;

import com.example.hrmsbackend.dtos.request.JobOpeningRequestDTO;
import com.example.hrmsbackend.dtos.request.ShareJobRequestDTO;
import com.example.hrmsbackend.dtos.request.ReferFriendRequestDTO;
import com.example.hrmsbackend.dtos.response.ApiResponse;
import com.example.hrmsbackend.dtos.response.JobOpeningResponseDTO;
import com.example.hrmsbackend.dtos.response.ShareJobResponseDTO;
import com.example.hrmsbackend.dtos.response.ReferralResponseDTO;
import com.example.hrmsbackend.services.JobOpeningService;
import com.example.hrmsbackend.services.JobSharingService;
import com.example.hrmsbackend.utils.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job-openings")
public class JobOpeningController {
    private JobOpeningService jobOpeningService;
    private JobSharingService jobSharingService;

    @Autowired
    public JobOpeningController(JobOpeningService jobOpeningService, JobSharingService jobSharingService) {
        this.jobOpeningService = jobOpeningService;
        this.jobSharingService = jobSharingService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('HR')")
    public ResponseEntity<ApiResponse<JobOpeningResponseDTO>> createJobOpening(
            @Valid @ModelAttribute JobOpeningRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        JobOpeningResponseDTO response = jobOpeningService.createJobOpening(request, userDetails);
        return ResponseEntity.ok(ResponseUtil.success(response, "Job opening created successfully", 201));
    }

    @PostMapping("/{jobId}/share")
    public ResponseEntity<ApiResponse<ShareJobResponseDTO>> shareJob(
            @PathVariable Long jobId,
            @Valid @RequestBody ShareJobRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        ShareJobResponseDTO response = jobSharingService.shareJob(jobId, request, userDetails);
        return ResponseEntity.ok(ResponseUtil.success(response, "Job shared successfully", 200));
    }

    @PostMapping("/{jobId}/refer")
    public ResponseEntity<ApiResponse<ReferralResponseDTO>> referFriend(
            @PathVariable Long jobId,
            @Valid @ModelAttribute ReferFriendRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        ReferralResponseDTO response = jobSharingService.referFriend(jobId, request, userDetails);
        return ResponseEntity.ok(ResponseUtil.success(response, "Referral submitted successfully", 201));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobOpeningResponseDTO>>> getAllJobOpenings() {
        List<JobOpeningResponseDTO> response = jobOpeningService.getAllJobOpenings();
        return ResponseEntity.ok(ResponseUtil.success(response, "Job openings fetched successfully", 200));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobOpeningResponseDTO>> getJobOpening(@PathVariable Long id) {
        JobOpeningResponseDTO response = jobOpeningService.getJobOpeningById(id);
        return ResponseEntity.ok(ResponseUtil.success(response, "Job opening fetched successfully", 200));
    }
}
