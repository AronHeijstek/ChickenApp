package com.app.chicken.service.impl;

import com.app.chicken.dto.ChickenDto;
import com.app.chicken.model.Chicken;
import com.app.chicken.model.User;
import com.app.chicken.repository.ChickenRepository;
import com.app.chicken.service.ChickenService;
import com.app.chicken.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChickenServiceImpl implements ChickenService {
    
    private final ChickenRepository chickenRepository;
    private final UserService userService;
    
    @Override
    public ChickenDto getChicken(String username) {
        User user = userService.getUserByUsername(username);
        Chicken chicken = chickenRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Chicken not found for user: " + username));
        
        return convertToDto(chicken);
    }
    
    @Override
    @Transactional
    public ChickenDto feedChicken(String username) {
        User user = userService.getUserByUsername(username);
        Chicken chicken = chickenRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Chicken not found for user: " + username));
        
        // Add experience for feeding
        chicken.addExperience(10);
        chicken.setLastFed(LocalDateTime.now());
        
        chickenRepository.save(chicken);
        log.info("Chicken fed for user: {}, new level: {}, experience: {}", 
                username, chicken.getLevel(), chicken.getExperience());
        
        return convertToDto(chicken);
    }
    
    @Override
    @Transactional
    public ChickenDto addExperience(String username, int xp) {
        User user = userService.getUserByUsername(username);
        Chicken chicken = chickenRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Chicken not found for user: " + username));
        
        boolean leveledUp = chicken.addExperience(xp);
        chickenRepository.save(chicken);
        
        if (leveledUp) {
            log.info("Chicken leveled up for user: {}, new level: {}", username, chicken.getLevel());
        }
        
        return convertToDto(chicken);
    }
    
    private ChickenDto convertToDto(Chicken chicken) {
        return ChickenDto.builder()
                .id(chicken.getId())
                .username(chicken.getUser().getUsername())
                .level(chicken.getLevel())
                .experience(chicken.getExperience())
                .lastFed(chicken.getLastFed())
                .build();
    }
} 