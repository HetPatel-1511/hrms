package com.example.hrmsbackend.controllers;

import com.example.hrmsbackend.dtos.Login;
import com.example.hrmsbackend.dtos.Register;
import com.example.hrmsbackend.dtos.request.EmployeeRequestDTO;
import com.example.hrmsbackend.dtos.response.AuthEmployeeResponseDTO;
import com.example.hrmsbackend.services.AuthService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;

    @Value("${application.security.jwt.access_token_expiration}")
    private int accessTokenExpiration;

    @Value("${application.security.jwt.refresh_token_expiration}")
    private int refreshTokenExpiration;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthEmployeeResponseDTO login(@RequestBody @Validated(Login.class) EmployeeRequestDTO employeeRequestDTO, HttpServletResponse response) {
        AuthEmployeeResponseDTO authEmployeeResponseDTO = authService.login(employeeRequestDTO);

        Cookie cookie1 = authService.newCookie("access_token", authEmployeeResponseDTO.getAccessToken(), accessTokenExpiration/1000, true, "/");
        response.addCookie(cookie1);

        Cookie cookie2 = authService.newCookie("refresh_token", authEmployeeResponseDTO.getRefreshToken(), refreshTokenExpiration/1000, true, "/");
        response.addCookie(cookie2);

        return authEmployeeResponseDTO;
    }

    @PostMapping("/register")
    public AuthEmployeeResponseDTO register(@RequestBody @Validated(Register.class) EmployeeRequestDTO employeeRequestDTO) {
        return authService.register(employeeRequestDTO);
    }

    @PostMapping("/refresh-token")
    public AuthEmployeeResponseDTO refreshToken(HttpServletResponse response, ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        Cookie[] cookies = httpRequest.getCookies();
        String token = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh_token")){
                    token=cookie.getValue();
                }
            }
        }

        AuthEmployeeResponseDTO authEmployeeResponseDTO = authService.refreshToken(token);

        Cookie cookie1 = authService.newCookie("access_token", authEmployeeResponseDTO.getAccessToken(), accessTokenExpiration/1000, true, "/");
        response.addCookie(cookie1);

        Cookie cookie2 = authService.newCookie("refresh_token", authEmployeeResponseDTO.getRefreshToken(), refreshTokenExpiration/1000, true, "/");
        response.addCookie(cookie2);

        return authEmployeeResponseDTO;
    }
}
