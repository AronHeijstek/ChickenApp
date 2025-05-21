package com.app.chicken.repository;

import com.app.chicken.model.Challenge;
import com.app.chicken.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    
    List<Challenge> findByUser(User user);
    
    List<Challenge> findByUserAndStatus(User user, Challenge.ChallengeStatus status);
} 