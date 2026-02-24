package com.example.hrmsbackend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "notifications_employees")
public class NotificationEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_notification_employee_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_notification_id")
    private Notification notification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_employee_id")
    private Employee employee;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    public NotificationEmployee() {
    }

    public NotificationEmployee(Notification notification, Employee employee) {
        this.notification = notification;
        this.employee = employee;
        this.isRead = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
}
