package com.example.hrmsbackend.filters;

import com.example.hrmsbackend.dtos.request.CustomUserDetails;
import com.example.hrmsbackend.services.CustomUserDetailsService;
import com.example.hrmsbackend.services.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.hrmsbackend.config.SecurityConfig.WHITE_LIST_URL;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Cookie[] cookies = httpRequest.getCookies();
        String token = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")){
                    token=cookie.getValue();
                }
            }
        }
        if (token != "" && token != null) {
            if (!jwtService.isExpired(token)) {
                Claims claims = jwtService.extractAllClaims(token);
                Object userName = claims.get("email");
                Collection<String> roles = (Collection<String>) claims.get("roles");

                CustomUserDetails user = (CustomUserDetails) customUserDetailsService.loadUserByUsername((String) userName);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                         user, null, roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
