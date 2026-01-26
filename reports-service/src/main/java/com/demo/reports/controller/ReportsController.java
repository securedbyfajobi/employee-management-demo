package com.demo.reports.controller;

import com.demo.reports.dto.*;
import com.demo.reports.service.ReportsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "Employee reports and analytics")
public class ReportsController {

    private final ReportsService reportsService;

    @GetMapping("/headcount")
    @Operation(summary = "Get headcount report", description = "Returns employee count by department and new hires")
    public ResponseEntity<HeadcountReportDTO> getHeadcountReport() {
        return ResponseEntity.ok(reportsService.getHeadcountReport());
    }

    @GetMapping("/salary")
    @Operation(summary = "Get salary summary", description = "Returns salary statistics across all employees")
    public ResponseEntity<SalarySummaryDTO> getSalarySummary() {
        return ResponseEntity.ok(reportsService.getSalarySummary());
    }

    @GetMapping("/departments")
    @Operation(summary = "Get all department stats", description = "Returns statistics for each department")
    public ResponseEntity<List<DepartmentStatsDTO>> getAllDepartmentStats() {
        return ResponseEntity.ok(reportsService.getDepartmentStats());
    }

    @GetMapping("/departments/{department}")
    @Operation(summary = "Get department stats", description = "Returns statistics for a specific department")
    public ResponseEntity<DepartmentStatsDTO> getDepartmentStats(@PathVariable String department) {
        return ResponseEntity.ok(reportsService.getDepartmentStats(department));
    }

    @GetMapping("/new-hires")
    @Operation(summary = "Get new hires", description = "Returns employees hired in the last N days (default 30)")
    public ResponseEntity<List<NewHireDTO>> getNewHires(@RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(reportsService.getNewHires(days));
    }
}
