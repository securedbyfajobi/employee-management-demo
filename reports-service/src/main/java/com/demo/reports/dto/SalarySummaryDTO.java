package com.demo.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalarySummaryDTO {
    private Long totalEmployees;
    private BigDecimal totalPayroll;
    private BigDecimal averageSalary;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private BigDecimal medianSalary;
}
