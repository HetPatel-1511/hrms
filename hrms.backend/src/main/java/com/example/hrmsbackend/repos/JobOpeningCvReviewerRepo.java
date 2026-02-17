package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.JobOpeningCvReviewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobOpeningCvReviewerRepo extends JpaRepository<JobOpeningCvReviewer, Long> {
}
