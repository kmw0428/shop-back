package com.project.shop.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            System.out.println("Attempting to authenticate user: " + authRequest.getUsername());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            System.out.println("Authentication successful for user: " + authRequest.getUsername());
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // 사용자 정보 가져오기
            User user = userService.getUserByUsername(authRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + authRequest.getUsername()));

            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            String refreshJwt = jwtUtil.generateRefreshToken(userDetails.getUsername());

            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", jwt);
            response.put("refreshToken", refreshJwt);
            response.put("username", userDetails.getUsername());
            response.put("userId", user.getId()); // 사용자 ID 추가

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            System.out.println("Authentication failed for user: " + authRequest.getUsername());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed!");
        }
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest refreshRequest) {
        try {
            System.out.println("Attempting to refresh token");
            String refreshToken = refreshRequest.getRefreshToken();
            String username = jwtUtil.extractUsername(refreshToken);

            if (jwtUtil.validateToken(refreshToken, username)) {
                String newAccessToken = jwtUtil.generateToken(username);
                return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken, null)); // 사용자 ID 없음
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token refresh failed!");
        }
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        if (userService.getUserByUsername(user.getUsername()).isPresent()) {
            return "Username is already taken!";
        }
        if (userService.getUserByEmail(user.getEmail()).isPresent()) {
            return "Email is already taken!";
        }
        userService.registerUser(user);
        return "Registration successful!";
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email) {
        try {
            userService.sendPasswordResetToken(email);
            return ResponseEntity.ok("Password reset token sent!");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    @PostMapping("/confirm-reset")
    public ResponseEntity<?> confirmReset(@RequestParam String token, @RequestParam String newPassword) {
        try {
            userService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Password has been reset successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Invalid token");
        }
    }
}
