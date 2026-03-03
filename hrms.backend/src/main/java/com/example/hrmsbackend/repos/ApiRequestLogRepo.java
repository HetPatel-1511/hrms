package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.ApiRequestLog;
import com.example.hrmsbackend.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiRequestLogRepo extends JpaRepository<ApiRequestLog, Long> {
}
