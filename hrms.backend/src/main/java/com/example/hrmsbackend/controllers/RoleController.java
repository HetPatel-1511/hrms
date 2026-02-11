package com.example.hrmsbackend.controllers;

import com.example.hrmsbackend.dtos.response.ApiResponse;
import com.example.hrmsbackend.dtos.response.RoleResponseDTO;
import com.example.hrmsbackend.services.RoleService;
import com.example.hrmsbackend.services.S3FileService;
import com.example.hrmsbackend.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponseDTO>>> getAll() {
        return ResponseEntity.ok(ResponseUtil.success(roleService.getAll(), "Employees fetched successfully", 200));
    }
}
