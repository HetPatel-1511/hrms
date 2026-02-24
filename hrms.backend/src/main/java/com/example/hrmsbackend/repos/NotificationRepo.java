package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n JOIN n.notificationEmployees ne WHERE ne.employee.id = :userId ORDER BY n.createdAt DESC")
    List<Notification> findByUserId(@Param("userId") Long userId);

    @Query("SELECT n FROM Notification n JOIN n.notificationEmployees ne WHERE ne.employee.id = :userId AND ne.isRead = false ORDER BY n.createdAt DESC")
    List<Notification> findUnreadByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(ne) FROM NotificationEmployee ne WHERE ne.employee.id = :userId AND ne.isRead = false")
    Long countUnreadByUserId(@Param("userId") Long userId);

    @Query("SELECT n FROM Notification n JOIN n.notificationEmployees ne WHERE n.id = :notificationId AND ne.employee.id = :userId")
    Optional<Notification> findByIdAndUserId(@Param("notificationId") Long notificationId, @Param("userId") Long userId);
}
