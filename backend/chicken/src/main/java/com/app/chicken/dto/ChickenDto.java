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
public class ChickenDto {
    private Long id;
    private String username;
    private Integer level;
    private Integer experience;
    private LocalDateTime lastFed;
} 