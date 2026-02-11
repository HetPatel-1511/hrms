package com.example.hrmsbackend.services;

import com.example.hrmsbackend.dtos.response.DocumentTypeResponseDTO;
import com.example.hrmsbackend.mappers.EntityMapper;
import com.example.hrmsbackend.repos.DocumentTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentTypeService {
    private DocumentTypeRepo documentTypeRepo;
    private EntityMapper entityMapper;

    @Autowired
    public DocumentTypeService(DocumentTypeRepo documentTypeRepo, EntityMapper entityMapper) {
        this.documentTypeRepo = documentTypeRepo;
        this.entityMapper = entityMapper;
    }

    public List<DocumentTypeResponseDTO> getAll() {
        return entityMapper.toDocumentTypeResponseDTOList(documentTypeRepo.findAll());
    }
}
