package com.app.chicken.client;

import com.app.chicken.dto.TransactionDto;
import com.app.chicken.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class TppApiClient {
    
    private final WebClient webClient;
    
    public TppApiClient(@Value("${tpp.api.base-url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
    
    /**
     * Fetches user details from TPP API
     * @param username User's username
     * @return User information from TPP API
     */
    public Mono<UserDto> getUserDetails(String username) {
        return webClient.get()
                .uri("/users/{username}", username)
                .retrieve()
                .bodyToMono(UserDto.class);
    }
    
    /**
     * Fetches all transactions for a user from TPP API
     * @param username User's username
     * @return List of transactions
     */
    public Mono<List<TransactionDto>> getUserTransactions(String username) {
        return webClient.get()
                .uri("/users/{username}/transactions", username)
                .retrieve()
                .bodyToFlux(TransactionDto.class)
                .collectList();
    }
} 