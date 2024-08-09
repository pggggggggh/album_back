package com.pgh.album_back.controller;

import com.pgh.album_back.dto.LoginRequestDTO;
import com.pgh.album_back.dto.RegisterRequestDTO;
import com.pgh.album_back.dto.UserInfoResponseDTO;
import com.pgh.album_back.entity.User;
import com.pgh.album_back.repository.UserRepository;
import com.pgh.album_back.security.JwtUtil;
import com.pgh.album_back.service.AuthService;
import com.pgh.album_back.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            return ResponseEntity.ok(userService.register(registerRequestDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            return ResponseEntity.ok(authService.login(loginRequestDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return ResponseEntity.ok(new UserInfoResponseDTO(user.getUsername(), user.getNickname(), user.getRole().toString()));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String,String> request) {
        String token = request.get("token");
        if (!jwtUtil.validateToken(token)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String username = jwtUtil.getUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return ResponseEntity.ok(authService.refreshToken(username));
    }

}
