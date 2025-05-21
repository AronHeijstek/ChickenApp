package com.app.chicken.service.impl;

import com.app.chicken.dto.ChallengeDto;
import com.app.chicken.dto.TransactionDto;
import com.app.chicken.dto.challenge.CreateChallengeRequest;
import com.app.chicken.dto.challenge.UpdateChallengeStatusRequest;
import com.app.chicken.model.Challenge;
import com.app.chicken.model.User;
import com.app.chicken.repository.ChallengeRepository;
import com.app.chicken.service.ChallengeService;
import com.app.chicken.service.ChickenService;
import com.app.chicken.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {
    
    private final ChallengeRepository challengeRepository;
    private final UserService userService;
    private final ChickenService chickenService;
    
    @Override
    @Transactional
    public ChallengeDto createChallenge(String username, CreateChallengeRequest request) {
        User user = userService.getUserByUsername(username);
        
        // Set XP reward based on difficulty
        int xpReward = getXpRewardForDifficulty(request.getDifficulty());
        
        // Set start date if not provided
        LocalDateTime startDate = request.getStartDate() != null ? 
                request.getStartDate() : LocalDateTime.now();
        
        // Calculate end date
        LocalDateTime endDate = startDate.plusDays(request.getDurationDays());
        
        // Ensure limit and spentAmount are positive values
        double limit = Math.abs(request.getLimit());
        double spentAmount = Math.abs(request.getSpentAmount());
        
        Challenge challenge = Challenge.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .difficulty(request.getDifficulty())
                .xpReward(xpReward)
                .durationDays(request.getDurationDays())
                .startDate(startDate)
                .endDate(endDate)
                .status(Challenge.ChallengeStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .limit(limit)
                .category(request.getCategory())
                .spentAmount(spentAmount)
                .type(request.getType())
                .build();
        
        Challenge savedChallenge = challengeRepository.save(challenge);
        log.info("Challenge created for user: {}, id: {}, title: {}", 
                username, savedChallenge.getId(), savedChallenge.getTitle());
        
        return convertToDto(savedChallenge);
    }
    
    @Override
    public List<ChallengeDto> getChallenges(String username) {
        // Update expired challenges first
        updateExpiredChallenges(username);
        
        User user = userService.getUserByUsername(username);
        
        return challengeRepository.findByUser(user).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ChallengeDto> getActiveChallenges(String username) {
        // Update expired challenges first
        updateExpiredChallenges(username);
        
        User user = userService.getUserByUsername(username);
        
        return challengeRepository.findByUserAndStatus(user, Challenge.ChallengeStatus.ACTIVE).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public ChallengeDto updateChallengeStatus(String username, Long challengeId, UpdateChallengeStatusRequest request) {
        User user = userService.getUserByUsername(username);
        
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found with id: " + challengeId));
        
        // Verify the challenge belongs to the user
        if (!challenge.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("Challenge does not belong to user: " + username);
        }
        
        // Update status
        challenge.setStatus(request.getStatus());
        
        // If completed, reward XP to chicken
        if (request.getStatus() == Challenge.ChallengeStatus.COMPLETED) {
            chickenService.addExperience(username, challenge.getXpReward());
            log.info("Challenge completed for user: {}, id: {}, XP rewarded: {}", 
                    username, challengeId, challenge.getXpReward());
        }
        
        Challenge updatedChallenge = challengeRepository.save(challenge);
        
        return convertToDto(updatedChallenge);
    }
    
    @Override
    @Transactional
    public void processTransactionForChallenges(String username, TransactionDto transaction) {
        User user = userService.getUserByUsername(username);
        
        // Find active challenges for this user with the same category
        List<Challenge> activeChallenges = challengeRepository.findByUserAndStatus(user, Challenge.ChallengeStatus.ACTIVE)
                .stream()
                .filter(challenge -> challenge.getCategory().equalsIgnoreCase(transaction.getCategory()))
                .collect(Collectors.toList());
        
        if (activeChallenges.isEmpty()) {
            System.out.println("Challenge not found for user: " + username);
            return; // No active challenges for this category
        }
        
        for (Challenge challenge : activeChallenges) {
            // Only process if within challenge time frame
            if (challenge.getStartDate().isBefore(transaction.getDatetime().plusHours(2)) &&
                    challenge.getEndDate().isAfter(transaction.getDatetime().plusHours(2))) {
                
                // Update the spent amount - store as absolute value for easier comparison
                challenge.setSpentAmount(challenge.getSpentAmount() + Math.abs(transaction.getAmount()));
                
                // Evaluate if the challenge is still met or has failed
                boolean challengeMet = evaluateChallenge(challenge);
                
                if (!challengeMet) {
                    challenge.setStatus(Challenge.ChallengeStatus.FAILED);
                    log.info("Challenge failed for user: {}, id: {}, title: {}", 
                            username, challenge.getId(), challenge.getTitle());
                }
                
                challengeRepository.save(challenge);
            }
        }
    }
    
    @Override
    @Transactional
    public void updateExpiredChallenges(String username) {
        User user = userService.getUserByUsername(username);
        LocalDateTime now = LocalDateTime.now();
        
        List<Challenge> expiredChallenges = challengeRepository.findByUserAndStatus(user, Challenge.ChallengeStatus.ACTIVE)
                .stream()
                .filter(challenge -> challenge.getEndDate().isBefore(now))
                .collect(Collectors.toList());
        
        for (Challenge challenge : expiredChallenges) {
            // If challenge end date has passed, evaluate if it was met
            boolean challengeMet = evaluateChallenge(challenge);
            
            if (challengeMet) {
                challenge.setStatus(Challenge.ChallengeStatus.COMPLETED);
                chickenService.addExperience(username, challenge.getXpReward());
                log.info("Challenge completed for user: {}, id: {}, XP rewarded: {}", 
                        username, challenge.getId(), challenge.getXpReward());
            } else {
                challenge.setStatus(Challenge.ChallengeStatus.FAILED);
                log.info("Challenge failed for user: {}, id: {}", username, challenge.getId());
            }
            
            challengeRepository.save(challenge);
        }
    }
    
    @Override
    public boolean evaluateChallenge(Challenge challenge) {
        // Note: The spentAmount is stored as a positive value (absolute value of negative amounts),
        // while the limit is also a positive value threshold
        switch (challenge.getType()) {
            case SPEND:
                // For SPEND challenges, user must spend at least the limit amount
                return challenge.getSpentAmount() >= challenge.getLimit();
            case DONT_SPEND:
                // For DONT_SPEND challenges, user must spend less than the limit
                return challenge.getSpentAmount() <= challenge.getLimit();
            default:
                log.error("Unknown challenge type: {}", challenge.getType());
                return false;
        }
    }
    
    private int getXpRewardForDifficulty(Challenge.ChallengeDifficulty difficulty) {
        return switch (difficulty) {
            case EASY -> 10;
            case MEDIUM -> 50;
            case HARD -> 100;
        };
    }
    
    private ChallengeDto convertToDto(Challenge challenge) {
        return ChallengeDto.builder()
                .id(challenge.getId())
                .username(challenge.getUser().getUsername())
                .title(challenge.getTitle())
                .description(challenge.getDescription())
                .difficulty(challenge.getDifficulty())
                .xpReward(challenge.getXpReward())
                .durationDays(challenge.getDurationDays())
                .startDate(challenge.getStartDate())
                .endDate(challenge.getEndDate())
                .status(challenge.getStatus())
                .createdAt(challenge.getCreatedAt())
                .limit(challenge.getLimit())
                .spentAmount(challenge.getSpentAmount())
                .category(challenge.getCategory())
                .type(challenge.getType())
                .build();
    }
} 