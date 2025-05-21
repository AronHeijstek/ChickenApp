package com.app.chicken.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service interface for Kafka subscription management
 */
@Service
public interface KafkaManager {
    
    /**
     * Subscribe to a Kafka topic for a user
     * @param username Username to subscribe to
     * @return Mono of boolean indicating success
     */
    Mono<Boolean> subscribeUserToTransactionTopic(String username);
    
    /**
     * Get available Kafka topics
     * @return Mono containing a set of topic names
     */
    Mono<java.util.Set<String>> getAvailableTopics();
} 