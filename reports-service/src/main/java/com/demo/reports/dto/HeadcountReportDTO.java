package com.demo.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeadcountReportDTO {
    private Long totalEmployees;
    private Map<String, Long> byDepartment;
    private Long newHiresThisMonth;
    private Long newHiresThisYear;
}
