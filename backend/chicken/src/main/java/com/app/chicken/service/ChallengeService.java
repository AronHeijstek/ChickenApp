package com.app.chicken.service;

import com.app.chicken.dto.ChallengeDto;
import com.app.chicken.dto.TransactionDto;
import com.app.chicken.dto.challenge.CreateChallengeRequest;
import com.app.chicken.dto.challenge.UpdateChallengeStatusRequest;
import com.app.chicken.model.Challenge;

import java.util.List;

public interface ChallengeService {
    
    /**
     * Create a new challenge
     * @param username Username
     * @param request Challenge creation request
     * @return Created challenge DTO
     */
    ChallengeDto createChallenge(String username, CreateChallengeRequest request);
    
    /**
     * Get all challenges for a user
     * @param username Username
     * @return List of challenge DTOs
     */
    List<ChallengeDto> getChallenges(String username);
    
    /**
     * Get active challenges for a user
     * @param username Username
     * @return List of active challenge DTOs
     */
    List<ChallengeDto> getActiveChallenges(String username);
    
    /**
     * Update challenge status
     * @param username Username
     * @param challengeId Challenge ID
     * @param request Status update request
     * @return Updated challenge DTO
     */
    ChallengeDto updateChallengeStatus(String username, Long challengeId, UpdateChallengeStatusRequest request);
    
    /**
     * Process a transaction for challenges
     * Evaluates if transaction affects any user challenges
     * @param username Username
     * @param transaction Transaction DTO
     */
    void processTransactionForChallenges(String username, TransactionDto transaction);
    
    /**
     * Update the status of expired challenges
     * This should be called before retrieving challenges
     * @param username Username
     */
    void updateExpiredChallenges(String username);
    
    /**
     * Evaluate a challenge against its spending criteria
     * @param challenge The challenge to evaluate
     * @return true if the challenge criteria is still met, false otherwise
     */
    boolean evaluateChallenge(Challenge challenge);
} 