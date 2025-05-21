package com.app.chicken.service;

import com.app.chicken.dto.UserDto;
import com.app.chicken.dto.auth.AuthRequest;
import com.app.chicken.dto.auth.AuthResponse;
import com.app.chicken.model.User;

public interface UserService {
    
    /**
     * Register a new user
     * @param authRequest Authentication request with username and password
     * @return Authentication response with JWT token
     */
    AuthResponse register(AuthRequest authRequest);
    
    /**
     * Login a user
     * @param authRequest Authentication request with username and password
     * @return Authentication response with JWT token
     */
    AuthResponse login(AuthRequest authRequest);
    
    /**
     * Get user by username
     * @param username Username
     * @return User
     */
    User getUserByUsername(String username);
    
    /**
     * Get user DTO by username
     * @param username Username
     * @return User DTO
     */
    UserDto getUserDtoByUsername(String username);
    
    /**
     * Update user's login statistics
     * @param username Username
     */
    void updateLoginStats(String username);
} 