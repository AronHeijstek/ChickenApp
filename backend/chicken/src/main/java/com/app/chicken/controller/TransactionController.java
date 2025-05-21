package com.app.chicken.controller;

import com.app.chicken.dto.SpendingOverviewDto;
import com.app.chicken.dto.TransactionDto;
import com.app.chicken.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Transaction operations")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {
    
    private final TransactionService transactionService;
    
    @GetMapping
    @Operation(summary = "Get user transactions", description = "Retrieves all transactions for the authenticated user")
    public ResponseEntity<List<TransactionDto>> getTransactions(Authentication authentication) {
        return ResponseEntity.ok(transactionService.getTransactions(authentication.getName()));
    }
    
    @GetMapping("/spending-overview")
    @Operation(summary = "Get spending overview", description = "Retrieves spending overview for the authenticated user")
    public ResponseEntity<SpendingOverviewDto> getSpendingOverview(Authentication authentication) {
        return ResponseEntity.ok(transactionService.getSpendingOverview(authentication.getName()));
    }
} 