package com.app.chicken.service.impl;

import com.app.chicken.client.TppApiClient;
import com.app.chicken.dto.UserDto;
import com.app.chicken.dto.auth.AuthRequest;
import com.app.chicken.dto.auth.AuthResponse;
import com.app.chicken.model.Chicken;
import com.app.chicken.model.User;
import com.app.chicken.repository.ChickenRepository;
import com.app.chicken.repository.UserRepository;
import com.app.chicken.security.JwtUtils;
import com.app.chicken.service.KafkaManager;
import com.app.chicken.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final ChickenRepository chickenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final TppApiClient tppApiClient;
    private final KafkaManager kafkaManager;
    
    @Override
    @Transactional
    public AuthResponse register(AuthRequest authRequest) {
        if (userRepository.existsByUsername(authRequest.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        // Fetch user details from TPP API
        UserDto tppUser = tppApiClient.getUserDetails(authRequest.getUsername())
                .block();
        
        if (tppUser == null) {
            throw new IllegalArgumentException("User not found in TPP API");
        }
        
        // Create user entity
        User user = User.builder()
                .username(authRequest.getUsername())
                .password(passwordEncoder.encode(authRequest.getPassword()))
                .name(tppUser.getName())
                .iban(tppUser.getIban())
                .balance(tppUser.getBalance())
                .createdAt(LocalDateTime.now())
                .lastLoginDate(LocalDateTime.now())
                .consecutiveLoginDays(1)
                .build();
        
        // Save user
        User savedUser = userRepository.save(user);
        
        // Create chicken for the user
        Chicken chicken = Chicken.builder()
                .user(savedUser)
                .lastFed(LocalDateTime.now())
                .build();
        
        chickenRepository.save(chicken);
        
        // Subscribe to Kafka topic for the user
        String username = authRequest.getUsername();

        // Subscribe to user's topic
        kafkaManager.subscribeUserToTransactionTopic(username)
                .subscribe(success -> {
                    if (success) {
                        log.info("Successfully subscribed to topic for user: {}", username);
                    } else {
                        log.error("Failed to subscribe to topic for user: {}", username);
                    }
                });
        
        // Generate JWT token
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(
                (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal());
        
        return new AuthResponse(authRequest.getUsername(), jwt, "Bearer");
    }
    
    @Override
    @Transactional
    public AuthResponse login(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(
                (org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal());
        
        // Update login statistics
        updateLoginStats(authRequest.getUsername());
        
        return new AuthResponse(authRequest.getUsername(), jwt, "Bearer");
    }
    
    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
    
    @Override
    public UserDto getUserDtoByUsername(String username) {
        User user = getUserByUsername(username);
        
        return UserDto.builder()
                .username(user.getUsername())
                .name(user.getName())
                .iban(user.getIban())
                .balance(user.getBalance())
                .createdAt(user.getCreatedAt())
                .consecutiveLoginDays(user.getConsecutiveLoginDays())
                .build();
    }
    
    @Override
    @Transactional
    public void updateLoginStats(String username) {
        User user = getUserByUsername(username);
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastLogin = user.getLastLoginDate();
        
        // Update consecutive login days
        if (lastLogin != null) {
            // If last login was yesterday, increment consecutive days
            if (ChronoUnit.DAYS.between(lastLogin.toLocalDate(), now.toLocalDate()) == 1) {
                user.setConsecutiveLoginDays(user.getConsecutiveLoginDays() + 1);
            } 
            // If last login was not yesterday (more than 1 day gap), reset to 1
            else if (ChronoUnit.DAYS.between(lastLogin.toLocalDate(), now.toLocalDate()) > 1) {
                user.setConsecutiveLoginDays(1);
            }
            // If login is same day, leave consecutive days as is
        }
        
        user.setLastLoginDate(now);
        userRepository.save(user);
    }
} 