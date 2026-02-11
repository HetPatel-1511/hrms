package com.example.hrmsbackend.services;

import com.example.hrmsbackend.dtos.response.RoleResponseDTO;
import com.example.hrmsbackend.mappers.EntityMapper;
import com.example.hrmsbackend.repos.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private RoleRepo roleRepo;
    private EntityMapper entityMapper;

    @Autowired
    public RoleService(RoleRepo roleRepo, EntityMapper entityMapper) {
        this.roleRepo = roleRepo;
        this.entityMapper = entityMapper;
    }

    public List<RoleResponseDTO> getAll() {
        return entityMapper.toRoleResponseDTOList(roleRepo.findAll());
    }
}
