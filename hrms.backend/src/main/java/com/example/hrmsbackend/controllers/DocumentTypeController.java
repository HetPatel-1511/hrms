package com.example.hrmsbackend.controllers;

import com.example.hrmsbackend.dtos.response.ApiResponse;
import com.example.hrmsbackend.dtos.response.DocumentTypeResponseDTO;
import com.example.hrmsbackend.services.DocumentTypeService;
import com.example.hrmsbackend.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/document-types")
public class DocumentTypeController {
    private DocumentTypeService documentTypeService;

    @Autowired
    public DocumentTypeController(DocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DocumentTypeResponseDTO>>> getAll() {
        return ResponseEntity.ok(ResponseUtil.success(documentTypeService.getAll(), "Document types fetched successfully", 200));
    }
}
