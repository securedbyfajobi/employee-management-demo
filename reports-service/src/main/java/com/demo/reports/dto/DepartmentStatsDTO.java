package com.demo.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentStatsDTO {
    private String department;
    private Long employeeCount;
    private BigDecimal totalSalary;
    private BigDecimal averageSalary;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
}
