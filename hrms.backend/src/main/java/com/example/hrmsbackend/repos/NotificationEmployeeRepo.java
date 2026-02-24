package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.NotificationEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationEmployeeRepo extends JpaRepository<NotificationEmployee, Long> {

    List<NotificationEmployee> findByEmployeeId(Long employeeId);

    List<NotificationEmployee> findByNotificationId(Long notificationId);

    Optional<NotificationEmployee> findByNotificationIdAndEmployeeId(Long notificationId, Long employeeId);
}
