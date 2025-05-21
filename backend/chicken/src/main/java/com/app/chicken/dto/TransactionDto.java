package com.app.chicken.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private String transactionIdentifier;
    private Double amount;
    private String category;
    private String receiverIban;
    private String receiverName;
    private String receiverAccountId;
    private String description;
    private LocalDateTime datetime;
    private String status;
} 