package com.pgh.album_back.service;

import com.pgh.album_back.dto.LoginRequestDTO;
import com.pgh.album_back.dto.TokenDTO;
import com.pgh.album_back.entity.User;
import com.pgh.album_back.repository.UserRepository;
import com.pgh.album_back.security.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public TokenDTO login(LoginRequestDTO loginRequestDTO) {
        String username = loginRequestDTO.getUsername();
        String password = loginRequestDTO.getPassword();
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }

        return jwtUtil.createTokens(user.getUsername());
    }
}
