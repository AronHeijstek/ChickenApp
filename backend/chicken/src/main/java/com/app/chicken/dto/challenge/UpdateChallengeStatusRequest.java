package com.app.chicken.dto.challenge;

import com.app.chicken.model.Challenge;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateChallengeStatusRequest {
    
    @NotNull(message = "Status is required")
    private Challenge.ChallengeStatus status;
} 