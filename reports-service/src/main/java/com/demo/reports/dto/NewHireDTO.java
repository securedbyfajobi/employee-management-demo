package com.demo.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewHireDTO {
    private Long id;
    private String fullName;
    private String email;
    private String department;
    private LocalDate hireDate;
}
