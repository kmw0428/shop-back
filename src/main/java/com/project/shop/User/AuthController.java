package com.project.shop.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            String jwt = jwtUtil.generateToken(authRequest.getUsername());
            System.out.println("JWT generated: " + jwt);
            String refreshJwt = jwtUtil.generateRefreshToken(authRequest.getUsername());
            System.out.println("Refresh JWT generated: " + refreshJwt);
            return ResponseEntity.ok(new AuthResponse(jwt, refreshJwt));  // 액세스 토큰과 리프레시 토큰 반환
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error during login!");
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
                return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken));
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
}
