package com.app.chicken.dto.challenge;

import com.app.chicken.model.Challenge;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Request DTO for creating a new challenge.
 * All monetary values (limit, spentAmount) are stored as positive numbers 
 * representing the absolute amount.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateChallengeRequest {
    
    @NotBlank(message = "Title cannot be blank")
    private String title;
    
    private String description;
    
    @NotNull(message = "Difficulty is required")
    private Challenge.ChallengeDifficulty difficulty;
    
    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 day")
    private Integer durationDays;
    
    private LocalDateTime startDate;

    /**
     * Spending limit for the challenge (positive value)
     */
    @NotNull(message = "Limit is required")
    @Positive(message = "Limit must be a positive number")
    private Double limit;

    /**
     * Current amount spent for the challenge (positive value)
     */
    @Builder.Default
    @Min(value = 0, message = "Spent amount cannot be negative")
    private Double spentAmount = 0.0;

    @NotBlank(message = "Category is required")
    private String category;
    
    @NotNull(message = "Challenge type is required")
    private Challenge.ChallengeType type;
} 