package com.app.chicken.repository;

import com.app.chicken.model.Chicken;
import com.app.chicken.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChickenRepository extends JpaRepository<Chicken, Long> {
    
    Optional<Chicken> findByUser(User user);
    
    boolean existsByUser(User user);
} 