package com.app.chicken.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "challenge")
public class Challenge {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String title;
    
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChallengeDifficulty difficulty;
    
    @Column(name = "xp_reward", nullable = false)
    private Integer xpReward;
    
    @Column(name = "duration_days", nullable = false)
    private Integer durationDays;

    @Column(name = "limit_amount", nullable = false)
    private Double limit;

    @Column(nullable = false)
    private String category;

    @Column(name = "spent_amount", nullable = false)
    @Builder.Default
    private Double spentAmount = 0.0;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChallengeType type;
    
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;
    
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChallengeStatus status;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public enum ChallengeDifficulty {
        EASY, MEDIUM, HARD
    }
    
    public enum ChallengeStatus {
        ACTIVE, COMPLETED, FAILED
    }
    
    public enum ChallengeType {
        SPEND, DONT_SPEND
    }
} 