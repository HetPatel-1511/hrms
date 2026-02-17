package com.example.hrmsbackend.services;

import com.example.hrmsbackend.dtos.request.EmailDetailsDTO;
import com.example.hrmsbackend.dtos.request.ShareJobRequestDTO;
import com.example.hrmsbackend.dtos.request.ReferFriendRequestDTO;
import com.example.hrmsbackend.dtos.response.ShareJobResponseDTO;
import com.example.hrmsbackend.dtos.response.ReferralResponseDTO;
import com.example.hrmsbackend.entities.*;
import com.example.hrmsbackend.exceptions.ResourceNotFoundException;
import com.example.hrmsbackend.mappers.EntityMapper;
import com.example.hrmsbackend.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobSharingService {
    private JobOpeningRepo jobOpeningRepo;
    private SharedJobRepo sharedJobRepo;
    private ReferalRepo referalRepo;
    private EmployeeRepo employeeRepo;
    private ConfigurationRepo configurationRepo;
    private EmailService emailService;
    private MediaService mediaService;
    private EntityMapper entityMapper;

    @Autowired
    public JobSharingService(JobOpeningRepo jobOpeningRepo, SharedJobRepo sharedJobRepo, 
                           ReferalRepo referalRepo, EmployeeRepo employeeRepo, 
                           ConfigurationRepo configurationRepo, EmailService emailService,
                           MediaService mediaService, EntityMapper entityMapper) {
        this.jobOpeningRepo = jobOpeningRepo;
        this.sharedJobRepo = sharedJobRepo;
        this.referalRepo = referalRepo;
        this.employeeRepo = employeeRepo;
        this.configurationRepo = configurationRepo;
        this.emailService = emailService;
        this.mediaService = mediaService;
        this.entityMapper = entityMapper;
    }

    @Transactional
    public ShareJobResponseDTO shareJob(Long jobId, ShareJobRequestDTO request, UserDetails userDetails) {
        JobOpening jobOpening = jobOpeningRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job opening not found with id: " + jobId));
        
        if (!jobOpening.getIsActive()) {
            throw new ResourceNotFoundException("Job opening is not active");
        }

        Employee sharedBy = employeeRepo.findByEmail(userDetails.getUsername());
        if (sharedBy == null) {
            throw new ResourceNotFoundException("User not found");
        }

        SharedJob sharedJob = new SharedJob();
        sharedJob.setEmail(request.getEmail());
        sharedJob.setJobOpening(jobOpening);
        sharedJob.setSharedBy(sharedBy);
        
        SharedJob savedSharedJob = sharedJobRepo.save(sharedJob);

        sendShareJobEmail(request.getEmail(), jobOpening);

        return entityMapper.toShareJobResponseDTO(savedSharedJob);
    }

    @Transactional
    public ReferralResponseDTO referFriend(Long jobId, ReferFriendRequestDTO request, UserDetails userDetails) {
        JobOpening jobOpening = jobOpeningRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job opening not found with id: " + jobId));
        
        if (!jobOpening.getIsActive()) {
            throw new ResourceNotFoundException("Job opening is not active");
        }

        Employee referredBy = employeeRepo.findByEmail(userDetails.getUsername());
        if (referredBy == null) {
            throw new ResourceNotFoundException("User not found");
        }

        if (request.getCvMedia() == null || request.getCvMedia().isEmpty()) {
            throw new ResourceNotFoundException("CV file is mandatory");
        }

        Media cvMedia = mediaService.upload(request.getCvMedia(), "cv", userDetails);

        Referal referral = new Referal();
        referral.setName(request.getName());
        referral.setEmail(request.getEmail());
        referral.setJobOpening(jobOpening);
        referral.setReferedBy(referredBy);
        referral.setCvMedia(cvMedia);

        Referal savedReferral = referalRepo.save(referral);

        sendReferralEmails(jobOpening, request, referredBy, cvMedia);

        return entityMapper.toReferralResponseDTO(savedReferral);
    }

    private void sendShareJobEmail(String recipientEmail, JobOpening jobOpening) {
        String subject = "Job Opportunity: " + jobOpening.getTitle();
        String body = "Dear Candidate,\n\n" +
                "We are pleased to share an exciting job opportunity with you:\n\n" +
                "Position: " + jobOpening.getTitle() + "\n" +
                "Summary: " + (jobOpening.getSummary() != null ? jobOpening.getSummary() : "N/A") + "\n\n" +
                "Please find the detailed job description attached.\n\n" +
                "Best regards,\n" +
                "HRMS";

        EmailDetailsDTO emailDetails = new EmailDetailsDTO();
        emailDetails.setRecipient(recipientEmail);
        emailDetails.setSubject(subject);
        emailDetails.setMsgBody(body);
        
        if (jobOpening.getDescriptionMedia() != null) {
            emailDetails.setAttachment(jobOpening.getDescriptionMedia().getUrl());
        }

        emailService.sendMailWithAttachment(emailDetails);
    }

    private void sendReferralEmails(JobOpening jobOpening, ReferFriendRequestDTO request, 
                                   Employee referredBy, Media cvMedia) {
        List<String> recipients = new ArrayList<>();
        
        recipients.add(jobOpening.getHr().getEmail());
        
        Configuration hrEmailConfig = configurationRepo.findByConfigKey("hr_email").orElse(null);
        if (hrEmailConfig != null) {
            recipients.add(hrEmailConfig.getConfigValue());
        }
        
        if (jobOpening.getCvReviewers() != null) {
            for (JobOpeningCvReviewer reviewer : jobOpening.getCvReviewers()) {
                recipients.add(reviewer.getReviewer().getEmail());
            }
        }

        String subject = "New Referral for " + jobOpening.getTitle();
        String body = "Dear Team,\n\n" +
                "A new referral has been received:\n\n" +
                "Job Title: " + jobOpening.getTitle() + "\n" +
                "Job ID: " + jobOpening.getId() + "\n" +
                "Referrer: " + referredBy.getName() + " (email: " + referredBy.getEmail() + ")\n" +
                "Friend Name: " + request.getName() + "\n" +
                "Friend Email: " + (request.getEmail() != null ? request.getEmail() : "N/A") + "\n";
        
        if (request.getShortNote() != null && !request.getShortNote().trim().isEmpty()) {
            body += "Short Note: " + request.getShortNote() + "\n";
        }
        
        body += "\nPlease find the CV attached for review.\n\n" +
                "Best regards,\n" +
                "HRMS";

        EmailDetailsDTO emailDetails = new EmailDetailsDTO();
        emailDetails.setRecipients(recipients);
        emailDetails.setSubject(subject);
        emailDetails.setMsgBody(body);
        emailDetails.setAttachment(cvMedia.getUrl());
        
        emailService.sendMailWithAttachment(emailDetails);
    }
}
