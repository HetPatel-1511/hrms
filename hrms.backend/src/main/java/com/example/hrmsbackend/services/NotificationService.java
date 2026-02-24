package com.example.hrmsbackend.services;

import com.example.hrmsbackend.dtos.request.NotificationCreateRequestDTO;
import com.example.hrmsbackend.dtos.request.BulkNotificationCreateRequestDTO;
import com.example.hrmsbackend.dtos.response.NotificationDTO;
import com.example.hrmsbackend.dtos.response.NotificationWithReadStatusDTO;
import com.example.hrmsbackend.entities.Employee;
import com.example.hrmsbackend.entities.Notification;
import com.example.hrmsbackend.entities.NotificationEmployee;
import com.example.hrmsbackend.exceptions.ResourceNotFoundException;
import com.example.hrmsbackend.mappers.EntityMapper;
import com.example.hrmsbackend.repos.EmployeeRepo;
import com.example.hrmsbackend.repos.NotificationEmployeeRepo;
import com.example.hrmsbackend.repos.NotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepo notificationRepo;
    private final NotificationEmployeeRepo notificationEmployeeRepo;
    private final EmployeeRepo employeeRepo;
    private final SimpMessagingTemplate messagingTemplate;
    private final EntityMapper entityMapper;

    @Autowired
    public NotificationService(NotificationRepo notificationRepo, NotificationEmployeeRepo notificationEmployeeRepo,
                               EmployeeRepo employeeRepo, SimpMessagingTemplate messagingTemplate, EntityMapper entityMapper) {
        this.notificationRepo = notificationRepo;
        this.notificationEmployeeRepo = notificationEmployeeRepo;
        this.employeeRepo = employeeRepo;
        this.messagingTemplate = messagingTemplate;
        this.entityMapper = entityMapper;
    }

    @Transactional
    public NotificationDTO createNotification(NotificationCreateRequestDTO request) {
        Notification notification = new Notification(request.getTitle(), request.getMessage());
        Notification savedNotification = notificationRepo.save(notification);

        if (request.getUserId() != null) {
            Employee user = employeeRepo.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));
            
            NotificationEmployee notificationEmployee = new NotificationEmployee(savedNotification, user);
            notificationEmployeeRepo.save(notificationEmployee);
            
            NotificationDTO dto = entityMapper.toNotificationDTO(savedNotification);
            messagingTemplate.convertAndSend("/topic/notifications/" + user.getId(), dto);
            return dto;
        } else {
            List<Employee> allEmployees = employeeRepo.findAll();
            for (Employee employee : allEmployees) {
                NotificationEmployee notificationEmployee = new NotificationEmployee(savedNotification, employee);
                notificationEmployeeRepo.save(notificationEmployee);
            }
            
            NotificationDTO dto = entityMapper.toNotificationDTO(savedNotification);
            messagingTemplate.convertAndSend("/topic/notifications/all", dto);
            return dto;
        }
    }

    @Transactional
    public NotificationDTO createBulkNotification(BulkNotificationCreateRequestDTO request) {
        Notification notification = new Notification(request.getTitle(), request.getMessage());
        Notification savedNotification = notificationRepo.save(notification);

        if (request.getSendAll() != null && request.getSendAll()) {
            List<Employee> allEmployees = employeeRepo.findAll();
            for (Employee employee : allEmployees) {
                NotificationEmployee notificationEmployee = new NotificationEmployee(savedNotification, employee);
                notificationEmployeeRepo.save(notificationEmployee);
            }
            
            NotificationDTO dto = entityMapper.toNotificationDTO(savedNotification);
            messagingTemplate.convertAndSend("/topic/notifications/all", dto);
            return dto;
        } else if (request.getUserIds() != null && !request.getUserIds().isEmpty()) {
            for (Long userId : request.getUserIds()) {
                Employee user = employeeRepo.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
                
                NotificationEmployee notificationEmployee = new NotificationEmployee(savedNotification, user);
                notificationEmployeeRepo.save(notificationEmployee);
                
                NotificationDTO dto = entityMapper.toNotificationDTO(savedNotification);
                messagingTemplate.convertAndSend("/topic/notifications/" + user.getId(), dto);
            }
            return entityMapper.toNotificationDTO(savedNotification);
        } else {
            throw new IllegalArgumentException("Either sendAll must be true or userIds list must be provided");
        }
    }

    @Transactional(readOnly = true)
    public List<NotificationWithReadStatusDTO> getNotificationsByUserId(Long userId) {
        employeeRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        return notificationEmployeeRepo.findByEmployeeId(userId)
                .stream()
                .map(entityMapper::toNotificationWithReadStatusDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationWithReadStatusDTO> getUnreadNotificationsByUserId(Long userId) {
        employeeRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        return notificationEmployeeRepo.findByEmployeeId(userId)
                .stream()
                .sorted((ne1, ne2) -> ne2.getNotification().getCreatedAt().compareTo(ne1.getNotification().getCreatedAt()))
                .filter(ne -> !ne.getIsRead())
                .map(entityMapper::toNotificationWithReadStatusDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long getUnreadCount(Long userId) {
        // Verify user exists
        employeeRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        return notificationRepo.countUnreadByUserId(userId);
    }

    @Transactional
    public NotificationWithReadStatusDTO markAsRead(Long notificationId, Long userId) {
        NotificationEmployee notificationEmployee = notificationEmployeeRepo.findByNotificationIdAndEmployeeId(notificationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification not found with ID: " + notificationId + " for user: " + userId));

        notificationEmployee.setIsRead(true);
        notificationEmployeeRepo.save(notificationEmployee);
        
        return entityMapper.toNotificationWithReadStatusDTO(notificationEmployee);
    }

    @Transactional
    public String markAllAsRead(Long userId) {
        employeeRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        List<NotificationEmployee> unreadNotifications = notificationEmployeeRepo.findByEmployeeId(userId)
                .stream().filter(ne -> !ne.getIsRead()).toList();
        unreadNotifications.forEach(ne -> ne.setIsRead(true));
        notificationEmployeeRepo.saveAll(unreadNotifications);

        return "All notifications marked as read";
    }

    @Transactional(readOnly = true)
    public NotificationWithReadStatusDTO getNotificationById(Long notificationId, Long userId) {
        NotificationEmployee notificationEmployee = notificationEmployeeRepo.findByNotificationIdAndEmployeeId(notificationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification not found with ID: " + notificationId + " for user: " + userId));
        
        return entityMapper.toNotificationWithReadStatusDTO(notificationEmployee);
    }

    @Transactional
    public String deleteNotification(Long notificationId, Long userId) {
        NotificationEmployee notificationEmployee = notificationEmployeeRepo.findByNotificationIdAndEmployeeId(notificationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification not found with ID: " + notificationId + " for user: " + userId));

        notificationEmployeeRepo.delete(notificationEmployee);
        return "Notification deleted successfully";
    }

    @Transactional
    public String clearAllNotifications(Long userId) {
        employeeRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        List<NotificationEmployee> notifications = notificationEmployeeRepo.findByEmployeeId(userId);
        notificationEmployeeRepo.deleteAll(notifications);

        return "All notifications cleared";
    }
}
