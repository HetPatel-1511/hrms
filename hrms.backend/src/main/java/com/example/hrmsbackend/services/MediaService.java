package com.example.hrmsbackend.services;

import com.example.hrmsbackend.entities.Employee;
import com.example.hrmsbackend.entities.Media;
import com.example.hrmsbackend.exceptions.ResourceNotFoundException;
import com.example.hrmsbackend.repos.EmployeeRepo;
import com.example.hrmsbackend.repos.MediaRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class MediaService {
    private S3FileService s3FileService;
    private MediaRepo mediaRepo;
    private EmployeeRepo employeeRepo;

    @Autowired
    public MediaService(S3FileService s3FileService, MediaRepo mediaRepo, EmployeeRepo employeeRepo) {
        this.s3FileService = s3FileService;
        this.mediaRepo = mediaRepo;
        this.employeeRepo = employeeRepo;
    }

    @Transactional
    public Media upload(MultipartFile file, String fileRole, UserDetails userDetails) {
        FileDetails fileDetails = getFileDetails(file, fileRole);
        String url = s3FileService.uploadFile(file, fileDetails.key());

        Employee uploadedBy = getEmployee(userDetails);

        Media media = saveMedia(uploadedBy, fileDetails, url);
        return media;
    }

    private Employee getEmployee(UserDetails userDetails) {
        Employee uploadedBy = employeeRepo.findByEmail(userDetails.getUsername());
        return uploadedBy;
    }

    @Transactional
    public boolean delete(Long mediaId) {
        Media media = getMedia(mediaId);
        media.setIsDeleted(true);
        return true;
    }


    private @NonNull Media saveMedia(Employee uploadedBy, FileDetails result, String url) {
        Media media = new Media();

        media.setOriginalName(result.originalName());
        media.setFileName(result.fileName());
        media.setFileKey(result.key());
        media.setMimeType(result.mimeType());
        media.setUploadedBy(uploadedBy);
        media.setUrl(url);

        mediaRepo.save(media);

        return media;
    }

    private @NonNull Media getMedia(Long mediaId) {
        Media media = mediaRepo.findById(mediaId).orElse(null);
        if (media == null) {
            throw new ResourceNotFoundException("Media not found");
        }
        return media;
    }

    private static @NonNull FileDetails getFileDetails(MultipartFile file, String fileRole) {
        String originalName = file.getOriginalFilename();
        String fileName = UUID.randomUUID() + "_" + originalName;
        String key = fileName;
        if (!fileRole.isEmpty()){
            key = fileRole + "/" + fileName;
        }
        String mimeType = file.getContentType();
        FileDetails result = new FileDetails(originalName, fileName, key, mimeType);
        return result;
    }

    private record FileDetails(String originalName, String fileName, String key, String mimeType) {
    }
}
