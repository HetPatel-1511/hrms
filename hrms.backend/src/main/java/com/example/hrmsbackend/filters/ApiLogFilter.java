package com.example.hrmsbackend.filters;

import com.example.hrmsbackend.repos.ApiRequestLogRepo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.catalina.filters.AddDefaultCharsetFilter;

import com.example.hrmsbackend.entities.ApiRequestLog;

@Component
public class ApiLogFilter extends OncePerRequestFilter {

    private ApiRequestLogRepo apiRequestLogRepo;

    @Autowired
    public ApiLogFilter(ApiRequestLogRepo apiRequestLogRepo) {
        this.apiRequestLogRepo = apiRequestLogRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
         throws ServletException, IOException {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        LocalDateTime startTime = LocalDateTime.now();
        String clientIp = request.getRemoteAddr();

        AddDefaultCharsetFilter.ResponseWrapper responseWrapper = new AddDefaultCharsetFilter.ResponseWrapper(response, "UTF-8");

        ApiRequestLog log = new ApiRequestLog();
        log.setMethod(method);
        log.setUri(uri);
        log.setTimestamp(LocalDateTime.now());
        log.setClientIp(clientIp);

        filterChain.doFilter(request, responseWrapper);

        LocalDateTime endTime = LocalDateTime.now();

        log.setResponseStatus(responseWrapper.getStatus());
        log.setDuration(java.time.Duration.between(startTime, endTime).toMillis());

        apiRequestLogRepo.save(log);
    }
}