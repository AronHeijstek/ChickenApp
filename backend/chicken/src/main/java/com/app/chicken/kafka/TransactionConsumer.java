package com.app.chicken.kafka;

import com.app.chicken.dto.TransactionDto;
import com.app.chicken.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TransactionConsumer implements KafkaMessageHandler {
    
    private final ObjectMapper objectMapper;
    private TransactionService transactionService;
    
    public TransactionConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    @Autowired
    public void setTransactionService(@Lazy TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    
    @Override
    public void handleMessage(String payload, String topic) {
        processMessage(payload, topic);
    }
    
    /**
     * Process message from Kafka
     * @param payload Message payload
     * @param topic Topic name
     */
    public void processMessage(String payload, String topic) {
        try {
            log.info("Received transaction message from topic {}: {}", topic, payload);
            TransactionDto transactionDto = objectMapper.readValue(payload, TransactionDto.class);
            
            // Extract username from the topic name (transactions.{username})
            if (topic != null && topic.startsWith("transactions.")) {
                String username = topic.substring("transactions.".length());
                transactionService.processTransaction(username, transactionDto);
            } else {
                log.error("Unable to determine username from topic: {}", topic);
            }
        } catch (Exception e) {
            log.error("Error processing transaction message: {}", e.getMessage(), e);
        }
    }
} 