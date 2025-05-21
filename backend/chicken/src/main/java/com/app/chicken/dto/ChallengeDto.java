package com.app.chicken.dto;

import com.app.chicken.model.Challenge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeDto {
    private Long id;
    private String username;
    private String title;
    private String description;
    private Challenge.ChallengeDifficulty difficulty;
    private Integer xpReward;
    private Integer durationDays;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Challenge.ChallengeStatus status;
    private LocalDateTime createdAt;
    private Double limit;
    private Double spentAmount;
    private String category;
    private Challenge.ChallengeType type;
} 