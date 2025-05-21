package com.app.chicken.kafka;

/**
 * Interface for handling Kafka messages
 */
public interface KafkaMessageHandler {
    
    /**
     * Process a Kafka message
     * @param payload Message content
     * @param topic Topic name the message was received from
     */
    void handleMessage(String payload, String topic);
} 