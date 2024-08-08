package com.pgh.album_back.service;

import com.pgh.album_back.constant.Role;
import com.pgh.album_back.dto.RegisterRequestDTO;
import com.pgh.album_back.dto.TokenDTO;
import com.pgh.album_back.entity.User;
import com.pgh.album_back.repository.UserRepository;
import com.pgh.album_back.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public TokenDTO register(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.existsByUsername(registerRequestDTO.getUsername())) {
            throw new DataIntegrityViolationException("Username is already taken");
        }
        if (userRepository.existsByNickname(registerRequestDTO.getNickname())) {
            throw new DataIntegrityViolationException("Nickname is already taken");
        }

        User user = new User();
        user.setUsername(registerRequestDTO.getUsername());
        user.setNickname(registerRequestDTO.getNickname());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        return jwtUtil.createTokens(user.getUsername());
    }
}
