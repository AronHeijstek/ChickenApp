package com.app.chicken.service;

import com.app.chicken.dto.ChickenDto;

public interface ChickenService {
    
    /**
     * Get chicken for a user
     * @param username Username
     * @return Chicken DTO
     */
    ChickenDto getChicken(String username);
    
    /**
     * Feed the chicken
     * @param username Username
     * @return Updated chicken DTO
     */
    ChickenDto feedChicken(String username);
    
    /**
     * Add experience to chicken
     * @param username Username
     * @param xp Experience to add
     * @return Updated chicken DTO
     */
    ChickenDto addExperience(String username, int xp);
} 