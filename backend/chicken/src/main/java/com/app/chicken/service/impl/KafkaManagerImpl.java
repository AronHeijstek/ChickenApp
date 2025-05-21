package com.app.chicken.service.impl;

import com.app.chicken.kafka.KafkaMessageHandler;
import com.app.chicken.service.KafkaManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class KafkaManagerImpl implements KafkaManager {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private final KafkaListenerContainerFactory<?> kafkaListenerContainerFactory;
    private final KafkaMessageHandler kafkaMessageHandler;
    
    // Keep track of active subscriptions and their containers
    private final Map<String, ConcurrentMessageListenerContainer<String, String>> activeContainers = new HashMap<>();

    public KafkaManagerImpl(
            KafkaListenerContainerFactory<?> kafkaListenerContainerFactory,
            KafkaMessageHandler kafkaMessageHandler) {
        this.kafkaListenerContainerFactory = kafkaListenerContainerFactory;
        this.kafkaMessageHandler = kafkaMessageHandler;
    }
    
    @Override
    public Mono<Set<String>> getAvailableTopics() {
        return Mono.fromCallable(() -> {
            Properties properties = new Properties();
            properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            
            try (AdminClient adminClient = AdminClient.create(properties)) {
                return new HashSet<>(adminClient.listTopics().names().get());
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error fetching Kafka topics: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to fetch Kafka topics", e);
            }
        });
    }
    
    @Override
    public Mono<Boolean> subscribeUserToTransactionTopic(String username) {
        return Mono.fromCallable(() -> {
            try {
                String topicName = "transactions." + username;
                
                // Already subscribed?
                if (activeContainers.containsKey(topicName)) {
                    log.info("Already subscribed to topic: {}", topicName);
                    return true;
                }
                
                // Create topic if it doesn't exist
                ensureTopicExists(topicName);
                
                // Create a message processor that delegates to the message handler
                MessageListener<String, String> messageListener = record -> 
                    kafkaMessageHandler.handleMessage(record.value(), record.topic());
                
                // Create a container directly using the factory
                ConcurrentMessageListenerContainer<String, String> container = 
                    (ConcurrentMessageListenerContainer<String, String>) kafkaListenerContainerFactory
                        .createContainer(topicName);
                
                // Set the message listener
                container.getContainerProperties().setMessageListener(messageListener);
                
                // Set a unique group ID so users don't conflict
                container.getContainerProperties().setGroupId("my_group." + username);
                
                // Set a unique bean name
                container.setBeanName("listener-" + topicName);
                
                // Save the container for future reference (if we need to stop it)
                activeContainers.put(topicName, container);
                
                // Start the container
                container.start();
                log.info("Started Kafka listener for topic: {}", topicName);
                
                return true;
            } catch (Exception e) {
                log.error("Error subscribing to topic: {}", e.getMessage(), e);
                return false;
            }
        });
    }
    
    /**
     * Ensure a topic exists by creating it if necessary
     * @param topicName Topic name to create
     */
    private void ensureTopicExists(String topicName) {
        try (AdminClient adminClient = AdminClient.create(
                Collections.singletonMap(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers))) {
            
            // Check if topic exists
            Set<String> existingTopics = adminClient.listTopics().names().get();
            if (!existingTopics.contains(topicName)) {
                log.info("Creating topic: {}", topicName);
                NewTopic newTopic = new NewTopic(topicName, 1, (short) 1);
                adminClient.createTopics(Collections.singleton(newTopic)).all().get();
                log.info("Topic {} created successfully", topicName);
            }
        } catch (Exception e) {
            log.warn("Could not create topic {}: {}", topicName, e.getMessage());
            // Continue anyway, as the broker might create it automatically
        }
    }
} 