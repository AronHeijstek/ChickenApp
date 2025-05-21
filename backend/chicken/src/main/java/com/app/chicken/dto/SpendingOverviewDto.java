package com.app.chicken.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Represents a user's spending overview (expenses only, not income).
 * All monetary values are positive numbers representing the absolute amount spent.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpendingOverviewDto {
    /**
     * Total spending amount for today (positive value)
     */
    private Double dailyTotal;
    
    /**
     * Total spending amount for the current week (positive value)
     */
    private Double weeklyTotal;
    
    /**
     * Total spending amount for the current month (positive value)
     */
    private Double monthlyTotal;
    
    /**
     * Total spending by category (all positive values)
     */
    private Map<String, Double> categoryTotals;
} 