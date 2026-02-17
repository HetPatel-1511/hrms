package com.example.hrmsbackend.services;

import com.example.hrmsbackend.dtos.request.JobOpeningRequestDTO;
import com.example.hrmsbackend.dtos.response.JobOpeningResponseDTO;
import com.example.hrmsbackend.entities.Employee;
import com.example.hrmsbackend.entities.JobOpening;
import com.example.hrmsbackend.entities.JobOpeningCvReviewer;
import com.example.hrmsbackend.entities.Media;
import com.example.hrmsbackend.exceptions.ResourceNotFoundException;
import com.example.hrmsbackend.mappers.EntityMapper;
import com.example.hrmsbackend.repos.EmployeeRepo;
import com.example.hrmsbackend.repos.JobOpeningCvReviewerRepo;
import com.example.hrmsbackend.repos.JobOpeningRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobOpeningService {
    private JobOpeningRepo jobOpeningRepo;
    private JobOpeningCvReviewerRepo jobOpeningCvReviewerRepo;
    private EmployeeRepo employeeRepo;
    private MediaService mediaService;
    private EntityMapper entityMapper;

    @Autowired
    public JobOpeningService(JobOpeningRepo jobOpeningRepo, JobOpeningCvReviewerRepo jobOpeningCvReviewerRepo, 
                           EmployeeRepo employeeRepo, MediaService mediaService, EntityMapper entityMapper) {
        this.jobOpeningRepo = jobOpeningRepo;
        this.jobOpeningCvReviewerRepo = jobOpeningCvReviewerRepo;
        this.employeeRepo = employeeRepo;
        this.mediaService = mediaService;
        this.entityMapper = entityMapper;
    }

    @Transactional
    public JobOpeningResponseDTO createJobOpening(JobOpeningRequestDTO request, UserDetails userDetails) {
        Media descriptionMedia = saveDescriptionMedia(request.getDescriptionMedia(), userDetails);

        Employee hr = getHr(request);

        JobOpening savedJobOpening = saveJobOpening(request, hr, descriptionMedia);

        saveCvReviewers(request, savedJobOpening);

        return entityMapper.toJobOpeningResponseDTO(savedJobOpening);
    }

    private void saveCvReviewers(JobOpeningRequestDTO request, JobOpening savedJobOpening) {
        if (request.getCvReviewerIds() != null && !request.getCvReviewerIds().isEmpty()) {
            List<JobOpeningCvReviewer> cvReviewers = new ArrayList<>();
            for (Long reviewerId : request.getCvReviewerIds()) {
                Employee reviewer = employeeRepo.findById(reviewerId)
                        .orElseThrow(() -> new ResourceNotFoundException("Reviewer employee not found with id: " + reviewerId));

                JobOpeningCvReviewer cvReviewer = new JobOpeningCvReviewer();
                cvReviewer.setJobOpening(savedJobOpening);
                cvReviewer.setReviewer(reviewer);
                cvReviewers.add(cvReviewer);
            }
            jobOpeningCvReviewerRepo.saveAll(cvReviewers);
            savedJobOpening.setCvReviewers(cvReviewers);
        }
    }

    private @NonNull Employee getHr(JobOpeningRequestDTO request) {
        Employee hr = employeeRepo.findById(request.getHrId())
                .orElseThrow(() -> new ResourceNotFoundException("HR employee not found with id: " + request.getHrId()));
        return hr;
    }

    private @NonNull JobOpening saveJobOpening(JobOpeningRequestDTO request, Employee hr, Media descriptionMedia) {
        JobOpening jobOpening = new JobOpening();
        jobOpening.setTitle(request.getTitle());
        jobOpening.setSummary(request.getSummary());
        jobOpening.setIsActive(true);
        jobOpening.setHr(hr);
        jobOpening.setDescriptionMedia(descriptionMedia);

        JobOpening savedJobOpening = jobOpeningRepo.save(jobOpening);
        return savedJobOpening;
    }

    private Media saveDescriptionMedia(MultipartFile descriptionMediaRequest, UserDetails userDetails) {
        Media descriptionMedia = mediaService.upload(descriptionMediaRequest, "job-description", userDetails);
        return descriptionMedia;
    }

    public JobOpeningResponseDTO getJobOpeningById(Long id) {
        JobOpening jobOpening = jobOpeningRepo.findById(id)
                .orElse(null);
        if (jobOpening == null) {
            throw new ResourceNotFoundException("Job opening not found with id: " + id);
        }
        return entityMapper.toJobOpeningResponseDTO(jobOpening);
    }

    public List<JobOpeningResponseDTO> getAllJobOpenings() {
        List<JobOpening> jobOpenings = jobOpeningRepo.findAll();
        return entityMapper.toJobOpeningResponseDTOList(jobOpenings);
    }
}
