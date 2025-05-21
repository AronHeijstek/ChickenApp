package com.app.chicken.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {
    
    @Id
    private String transactionIdentifier;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private Double amount;
    
    private String category;
    
    private String receiverIban;
    
    private String receiverName;
    
    private String receiverAccountId;
    
    private String description;
    
    private LocalDateTime datetime;
    
    private String status;
} 