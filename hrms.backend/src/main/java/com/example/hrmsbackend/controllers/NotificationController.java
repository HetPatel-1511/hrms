package com.example.hrmsbackend.controllers;

import com.example.hrmsbackend.dtos.request.NotificationCreateRequestDTO;
import com.example.hrmsbackend.dtos.request.BulkNotificationCreateRequestDTO;
import com.example.hrmsbackend.dtos.response.ApiResponse;
import com.example.hrmsbackend.dtos.response.NotificationDTO;
import com.example.hrmsbackend.dtos.response.NotificationWithReadStatusDTO;
import com.example.hrmsbackend.entities.Employee;
import com.example.hrmsbackend.exceptions.ResourceNotFoundException;
import com.example.hrmsbackend.repos.EmployeeRepo;
import com.example.hrmsbackend.services.NotificationService;
import com.example.hrmsbackend.utils.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final EmployeeRepo employeeRepo;

    @Autowired
    public NotificationController(NotificationService notificationService, EmployeeRepo employeeRepo) {
        this.notificationService = notificationService;
        this.employeeRepo = employeeRepo;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationDTO>> createNotification(
            @Valid @RequestBody NotificationCreateRequestDTO request) {
        NotificationDTO notification = notificationService.createNotification(request);
        return ResponseEntity.status(201)
                .body(ResponseUtil.success(notification, "Notification created successfully", 201));
    }

    @PostMapping("/bulk")
    public ResponseEntity<ApiResponse<NotificationDTO>> createBulkNotification(
            @Valid @RequestBody BulkNotificationCreateRequestDTO request) {
        NotificationDTO notification = notificationService.createBulkNotification(request);
        return ResponseEntity.status(201)
                .body(ResponseUtil.success(notification, "Bulk notification created successfully", 201));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationWithReadStatusDTO>>> getNotifications(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = extractUserIdFromUserDetails(userDetails);
        List<NotificationWithReadStatusDTO> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(ResponseUtil.success(notifications, "Notifications retrieved successfully", 200));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<NotificationWithReadStatusDTO>>> getNotificationsByUserId(
            @PathVariable Long userId) {
        List<NotificationWithReadStatusDTO> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(ResponseUtil.success(notifications, "Notifications retrieved successfully", 200));
    }

    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<List<NotificationWithReadStatusDTO>>> getUnreadNotifications(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = extractUserIdFromUserDetails(userDetails);
        List<NotificationWithReadStatusDTO> notifications = notificationService.getUnreadNotificationsByUserId(userId);
        return ResponseEntity.ok(ResponseUtil.success(notifications, "Unread notifications retrieved successfully", 200));
    }

    @GetMapping("/unread/count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = extractUserIdFromUserDetails(userDetails);
        Long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(ResponseUtil.success(count, "Unread count retrieved successfully", 200));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationWithReadStatusDTO>> getNotificationById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = extractUserIdFromUserDetails(userDetails);
        NotificationWithReadStatusDTO notification = notificationService.getNotificationById(id, userId);
        return ResponseEntity.ok(ResponseUtil.success(notification, "Notification retrieved successfully", 200));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationWithReadStatusDTO>> markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = extractUserIdFromUserDetails(userDetails);
        NotificationWithReadStatusDTO notification = notificationService.markAsRead(id, userId);
        return ResponseEntity.ok(ResponseUtil.success(notification, "Notification marked as read", 200));
    }

    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse<String>> markAllAsRead(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = extractUserIdFromUserDetails(userDetails);
        String result = notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(ResponseUtil.success(result, result, 200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteNotification(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = extractUserIdFromUserDetails(userDetails);
        String result = notificationService.deleteNotification(id, userId);
        return ResponseEntity.ok(ResponseUtil.success(result, result, 200));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> clearAllNotifications(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = extractUserIdFromUserDetails(userDetails);
        String result = notificationService.clearAllNotifications(userId);
        return ResponseEntity.ok(ResponseUtil.success(result, result, 200));
    }

    private Long extractUserIdFromUserDetails(UserDetails userDetails) {
        String email = userDetails.getUsername();
        Employee employee = employeeRepo.findByEmail(email);
        if (employee == null) {
            throw new ResourceNotFoundException("Employee not found with email: " + email);
        }
        return employee.getId();
    }
}
