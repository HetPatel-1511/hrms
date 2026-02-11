package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.Designation;
import com.example.hrmsbackend.entities.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTypeRepo extends JpaRepository<DocumentType, Long> {
}
