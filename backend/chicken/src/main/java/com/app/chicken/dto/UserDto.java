package com.app.chicken.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String username;
    private String name;
    private String iban;
    private Double balance;
    private LocalDateTime createdAt;
    private Integer consecutiveLoginDays;
} 