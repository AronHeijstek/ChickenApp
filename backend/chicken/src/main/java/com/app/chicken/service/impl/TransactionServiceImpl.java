package com.app.chicken.service.impl;

import com.app.chicken.client.TppApiClient;
import com.app.chicken.dto.SpendingOverviewDto;
import com.app.chicken.dto.TransactionDto;
import com.app.chicken.model.Transaction;
import com.app.chicken.model.User;
import com.app.chicken.repository.TransactionRepository;
import com.app.chicken.service.ChallengeService;
import com.app.chicken.service.TransactionService;
import com.app.chicken.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final TppApiClient tppApiClient;
    private final ChallengeService challengeService;
    
    @Override
    @Transactional
    public void processTransaction(String username, TransactionDto transactionDto) {
        log.info("Processing transaction for user: {}, transaction: {}", username, transactionDto);
        
        // Skip if transaction already exists
        if (transactionRepository.existsByTransactionIdentifier(transactionDto.getTransactionIdentifier())) {
            log.info("Transaction already exists: {}", transactionDto.getTransactionIdentifier());
            return;
        }
        
        User user = userService.getUserByUsername(username);
        
        Transaction transaction = Transaction.builder()
                .transactionIdentifier(transactionDto.getTransactionIdentifier())
                .user(user)
                .amount(transactionDto.getAmount())
                .category(transactionDto.getCategory())
                .receiverIban(transactionDto.getReceiverIban())
                .receiverName(transactionDto.getReceiverName())
                .receiverAccountId(transactionDto.getReceiverAccountId())
                .description(transactionDto.getDescription())
                .datetime(transactionDto.getDatetime().plusHours(2))
                .status(transactionDto.getStatus())
                .build();
        
        transactionRepository.save(transaction);
        
        // Process transaction for user's challenges
        challengeService.processTransactionForChallenges(username, transactionDto);
        
        log.info("Transaction processed and saved: {}", transaction.getTransactionIdentifier());
    }
    
    @Override
    @Transactional
    public void importInitialTransactions(String username) {
        User user = userService.getUserByUsername(username);
        
        tppApiClient.getUserTransactions(username)
                .subscribe(transactions -> {
                    for (TransactionDto transactionDto : transactions) {
                        if (!transactionRepository.existsByTransactionIdentifier(transactionDto.getTransactionIdentifier())) {
                            Transaction transaction = Transaction.builder()
                                    .transactionIdentifier(transactionDto.getTransactionIdentifier())
                                    .user(user)
                                    .amount(transactionDto.getAmount())
                                    .category(transactionDto.getCategory())
                                    .receiverIban(transactionDto.getReceiverIban())
                                    .receiverName(transactionDto.getReceiverName())
                                    .receiverAccountId(transactionDto.getReceiverAccountId())
                                    .description(transactionDto.getDescription())
                                    .datetime(transactionDto.getDatetime())
                                    .status(transactionDto.getStatus())
                                    .build();
                            
                            transactionRepository.save(transaction);
                            
                            // Process each initial transaction for challenges
                            challengeService.processTransactionForChallenges(username, transactionDto);
                        }
                    }
                    log.info("Imported {} initial transactions for user: {}", transactions.size(), username);
                });
    }
    
    @Override
    public List<TransactionDto> getTransactions(String username) {
        User user = userService.getUserByUsername(username);
        
        return transactionRepository.findByUser(user).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public SpendingOverviewDto getSpendingOverview(String username) {
        User user = userService.getUserByUsername(username);
        
        // Get time boundaries
        LocalDateTime today = LocalDate.now().atStartOfDay();
        LocalDateTime tomorrow = today.plusDays(1);
        LocalDateTime weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDateTime monthStart = today.withDayOfMonth(1);
        
        // Calculate totals - only includes expenses (negative amounts)
        // Results are absolute values for better readability
        Double dailyTotal = transactionRepository.findTotalSpentBetweenDates(user, today, tomorrow);
        Double weeklyTotal = transactionRepository.findTotalSpentBetweenDates(user, weekStart, tomorrow);
        Double monthlyTotal = transactionRepository.findTotalSpentBetweenDates(user, monthStart, tomorrow);
        
        // Get category totals - only includes expenses (negative amounts)
        List<Map<String, Object>> categorySpending = transactionRepository.findTotalSpendingByCategory(user);
        Map<String, Double> categoryTotals = new HashMap<>();
        
        for (Map<String, Object> entry : categorySpending) {
            String category = (String) entry.get("category");
            Double total = ((Number) entry.get("total")).doubleValue();
            // Store category totals as absolute values for better readability
            categoryTotals.put(category, Math.abs(total));
        }
        
        return SpendingOverviewDto.builder()
                .dailyTotal(dailyTotal != null ? dailyTotal : 0.0)
                .weeklyTotal(weeklyTotal != null ? weeklyTotal : 0.0)
                .monthlyTotal(monthlyTotal != null ? monthlyTotal : 0.0)
                .categoryTotals(categoryTotals)
                .build();
    }
    
    private TransactionDto convertToDto(Transaction transaction) {
        return TransactionDto.builder()
                .transactionIdentifier(transaction.getTransactionIdentifier())
                .amount(transaction.getAmount())
                .category(transaction.getCategory())
                .receiverIban(transaction.getReceiverIban())
                .receiverName(transaction.getReceiverName())
                .receiverAccountId(transaction.getReceiverAccountId())
                .description(transaction.getDescription())
                .datetime(transaction.getDatetime())
                .status(transaction.getStatus())
                .build();
    }
} 