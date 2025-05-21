package com.app.chicken.service;

import com.app.chicken.dto.SpendingOverviewDto;
import com.app.chicken.dto.TransactionDto;

import java.util.List;

public interface TransactionService {
    
    /**
     * Process a new transaction from Kafka
     * @param username Username
     * @param transactionDto Transaction DTO
     */
    void processTransaction(String username, TransactionDto transactionDto);
    
    /**
     * Import initial transactions for a user from TPP API
     * @param username Username
     */
    void importInitialTransactions(String username);
    
    /**
     * Get transactions for a user
     * @param username Username
     * @return List of transactions
     */
    List<TransactionDto> getTransactions(String username);
    
    /**
     * Get spending overview for a user
     * @param username Username
     * @return Spending overview
     */
    SpendingOverviewDto getSpendingOverview(String username);
} 