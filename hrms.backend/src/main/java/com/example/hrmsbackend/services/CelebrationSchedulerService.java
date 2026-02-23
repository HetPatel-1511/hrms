package com.example.hrmsbackend.services;

import com.example.hrmsbackend.entities.Employee;
import com.example.hrmsbackend.entities.Post;
import com.example.hrmsbackend.repos.EmployeeRepo;
import com.example.hrmsbackend.repos.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class CelebrationSchedulerService {

    private final EmployeeRepo employeeRepo;
    private final PostRepo postRepo;

    @Autowired
    public CelebrationSchedulerService(EmployeeRepo employeeRepo, PostRepo postRepo) {
        this.employeeRepo = employeeRepo;
        this.postRepo = postRepo;
    }

    // Run every day at 00:00
//    @Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(cron = "0 * 17 * * ?")
    @Transactional
    public void createDailyCelebrationPosts() {
        System.out.println("createDailyCelebrationPosts-=-=-==-");
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();
        List<Employee> employees = employeeRepo.findByBirthOrJoiningMonthAndDay(month, day);
        for (Employee e : employees) {
            if (e.getBirthDate() != null) {
                if (e.getBirthDate().getDayOfMonth() == today.getDayOfMonth() &&
                        e.getBirthDate().getMonthValue() == today.getMonthValue()) {
                    Post p = new Post();
                    p.setTitle("Happy Birthday " + e.getName());
                    p.setDescription("Happy birthday " + e.getName());
                    p.setAuthor(e);
                    p.setSystemGenerated(true);
                    postRepo.save(p);
                }
            }

            if (e.getJoiningDate() != null) {
                if (e.getJoiningDate().getDayOfMonth() == today.getDayOfMonth() &&
                        e.getJoiningDate().getMonthValue() == today.getMonthValue()) {
                    int years = Period.between(e.getJoiningDate(), today).getYears();
                    if (years >= 1) {
                        Post p = new Post();
                        p.setTitle(e.getName() + " completes " + years + " years at the organization");
                        p.setDescription(e.getName() + " completes " + years + " years at the organization");
                        p.setAuthor(e);
                        p.setSystemGenerated(true);
                        postRepo.save(p);
                    }
                }
            }
        }
    }
}
