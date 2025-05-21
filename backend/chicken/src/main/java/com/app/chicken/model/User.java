package com.app.chicken.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "users")
public class User {
    
    @Id
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    private String name;
    
    private String iban;
    
    private Double balance;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime lastLoginDate;
    
    private Integer consecutiveLoginDays;
} 