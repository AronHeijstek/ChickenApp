package com.app.chicken.controller;

import com.app.chicken.dto.ChallengeDto;
import com.app.chicken.dto.challenge.CreateChallengeRequest;
import com.app.chicken.dto.challenge.UpdateChallengeStatusRequest;
import com.app.chicken.service.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/challenges")
@RequiredArgsConstructor
@Tag(name = "Challenges", description = "Challenge operations")
@SecurityRequirement(name = "bearerAuth")
public class ChallengeController {
    
    private final ChallengeService challengeService;
    
    @PostMapping
    @Operation(summary = "Create a new challenge", description = "Creates a new challenge for the authenticated user")
    public ResponseEntity<ChallengeDto> createChallenge(
            Authentication authentication,
            @Valid @RequestBody CreateChallengeRequest request) {
        return ResponseEntity.ok(challengeService.createChallenge(authentication.getName(), request));
    }
    
    @GetMapping
    @Operation(summary = "Get all challenges", description = "Retrieves all challenges for the authenticated user")
    public ResponseEntity<List<ChallengeDto>> getChallenges(Authentication authentication) {
        return ResponseEntity.ok(challengeService.getChallenges(authentication.getName()));
    }
    
    @GetMapping("/active")
    @Operation(summary = "Get active challenges", description = "Retrieves active challenges for the authenticated user")
    public ResponseEntity<List<ChallengeDto>> getActiveChallenges(Authentication authentication) {
        return ResponseEntity.ok(challengeService.getActiveChallenges(authentication.getName()));
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "Update challenge status", description = "Updates the status of a challenge")
    public ResponseEntity<ChallengeDto> updateChallengeStatus(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody UpdateChallengeStatusRequest request) {
        return ResponseEntity.ok(challengeService.updateChallengeStatus(authentication.getName(), id, request));
    }
} 