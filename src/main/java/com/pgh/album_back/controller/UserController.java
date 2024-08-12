package com.pgh.album_back.controller;

import com.pgh.album_back.dto.LeaderboardsDTO;
import com.pgh.album_back.dto.LoginRequestDTO;
import com.pgh.album_back.dto.RegisterRequestDTO;
import com.pgh.album_back.dto.UserInfoResponseDTO;
import com.pgh.album_back.entity.Notification;
import com.pgh.album_back.entity.User;
import com.pgh.album_back.repository.NotificationRepository;
import com.pgh.album_back.repository.UserRepository;
import com.pgh.album_back.security.JwtUtil;
import com.pgh.album_back.service.AuthService;
import com.pgh.album_back.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final NotificationRepository notificationRepository;

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

    @Transactional
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        UserInfoResponseDTO userInfoResponseDTO = new UserInfoResponseDTO();
        userInfoResponseDTO.setUsername(user.getUsername());
        userInfoResponseDTO.setNickname(user.getNickname());
        userInfoResponseDTO.setRole(user.getRole().toString());
        userInfoResponseDTO.setPoint(user.getPoint());

        List<Notification> notifications = notificationRepository.findTop5ByUserOrderByCreatedAtDesc(user);
        notifications.forEach(notification -> {
            UserInfoResponseDTO.Notification dtoNotification = new UserInfoResponseDTO.Notification();
            dtoNotification.setTitle(notification.getTitle());
            dtoNotification.setContent(notification.getContent());
            dtoNotification.setSent(notification.getSent());
            dtoNotification.setCreatedAt(notification.getCreatedAt());
            notification.setSent(notification.getSent() + 1);

            userInfoResponseDTO.addNotification(dtoNotification);
        });

        return ResponseEntity.ok(userInfoResponseDTO);
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

    @GetMapping("/leaderboards")
    public ResponseEntity<?> leaderboards(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<User> users = userRepository.findByOrderByPointDesc(pageable);

        LeaderboardsDTO leaderboardsDTO = new LeaderboardsDTO();
        for (User user : users) {
            LeaderboardsDTO.User userDto = new LeaderboardsDTO.User();
            userDto.setUsername(user.getUsername());
            userDto.setNickname(user.getNickname());
            userDto.setPoint(user.getPoint());
            userDto.setRatingCount(user.getRatingCount());
            userDto.setReviewCount(user.getReviewCount());

            leaderboardsDTO.addUser(userDto);
        }

        return ResponseEntity.ok(leaderboardsDTO);
    }
}
