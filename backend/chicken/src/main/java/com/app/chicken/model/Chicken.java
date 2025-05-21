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
@Table(name = "chicken")
public class Chicken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", unique = true, nullable = false)
    private User user;
    
    @Builder.Default
    private Integer level = 1;
    
    @Builder.Default
    private Integer experience = 0;
    
    private LocalDateTime lastFed;
    
    /**
     * Adds experience to the chicken and levels up if necessary
     * @param xp amount of experience to add
     * @return true if the chicken leveled up, false otherwise
     */
    public boolean addExperience(int xp) {
        boolean leveledUp = false;
        
        this.experience += xp;
        
        // Level up logic - each level requires 100 XP
        if (this.experience >= 100) {
            int levelsGained = this.experience / 100;
            this.level += levelsGained;
            this.experience = this.experience % 100;
            leveledUp = true;
        }
        
        return leveledUp;
    }
} 