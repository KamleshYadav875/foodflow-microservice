package com.foodflow.identity_service.config;

import com.foodflow.identity_service.entity.User;
import com.foodflow.identity_service.enums.UserRole;
import com.foodflow.identity_service.exceptions.ResourceNotFoundException;
import com.foodflow.identity_service.jwt.JwtService;
import com.foodflow.identity_service.repository.UserRepository;
import com.foodflow.identity_service.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if(header == null || !header.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try{
            Long userId = jwtService.getUserId(token);
            Set<UserRole> roles = jwtService.getRoles(token);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            roles.stream()
                                    .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                                    .toList()
                            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        catch (Exception ex) {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }
}
