package com.app.chicken.controller;

import com.app.chicken.dto.auth.AuthRequest;
import com.app.chicken.dto.auth.AuthResponse;
import com.app.chicken.service.TransactionService;
import com.app.chicken.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication operations")
public class AuthController {
    
    private final UserService userService;
    private final TransactionService transactionService;
    
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user and returns a JWT token")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = userService.register(request);
        
        // Import initial transactions
        transactionService.importInitialTransactions(request.getUsername());
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    @Operation(summary = "Log in a user", description = "Authenticates a user and returns a JWT token")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
} 