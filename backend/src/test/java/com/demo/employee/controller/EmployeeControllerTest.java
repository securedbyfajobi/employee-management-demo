package com.demo.employee.controller;

import com.demo.employee.dto.EmployeeDTO;
import com.demo.employee.exception.DuplicateEmailException;
import com.demo.employee.exception.EmployeeNotFoundException;
import com.demo.employee.exception.GlobalExceptionHandler;
import com.demo.employee.model.Department;
import com.demo.employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private ObjectMapper objectMapper;
    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        employeeDTO = new EmployeeDTO(
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                Department.ENGINEERING,
                new BigDecimal("75000.00"),
                LocalDate.of(2023, 1, 15)
        );
    }

    @Test
    void getAllEmployees_ReturnsEmployeeList() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(Arrays.asList(employeeDTO));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email", is("john.doe@example.com")));
    }

    @Test
    void getEmployeeById_WhenExists_ReturnsEmployee() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(employeeDTO);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }

    @Test
    void getEmployeeById_WhenNotExists_Returns404() throws Exception {
        when(employeeService.getEmployeeById(99L)).thenThrow(new EmployeeNotFoundException(99L));

        mockMvc.perform(get("/api/employees/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found with id: 99"));
    }

    @Test
    void createEmployee_WithValidData_Returns201() throws Exception {
        EmployeeDTO newEmployee = new EmployeeDTO(
                null, "Jane", "Smith", "jane.smith@example.com",
                Department.MARKETING, new BigDecimal("65000.00"), LocalDate.of(2023, 6, 1)
        );
        EmployeeDTO savedEmployee = new EmployeeDTO(
                2L, "Jane", "Smith", "jane.smith@example.com",
                Department.MARKETING, new BigDecimal("65000.00"), LocalDate.of(2023, 6, 1)
        );

        when(employeeService.createEmployee(any(EmployeeDTO.class))).thenReturn(savedEmployee);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.email", is("jane.smith@example.com")));
    }

    @Test
    void createEmployee_WithDuplicateEmail_Returns409() throws Exception {
        when(employeeService.createEmployee(any(EmployeeDTO.class)))
                .thenThrow(new DuplicateEmailException("john.doe@example.com"));

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Employee with email 'john.doe@example.com' already exists"));
    }

    @Test
    void updateEmployee_WhenExists_ReturnsUpdatedEmployee() throws Exception {
        when(employeeService.updateEmployee(eq(1L), any(EmployeeDTO.class))).thenReturn(employeeDTO);

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")));
    }

    @Test
    void deleteEmployee_WhenExists_Returns204() throws Exception {
        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteEmployee_WhenNotExists_Returns404() throws Exception {
        doThrow(new EmployeeNotFoundException(99L)).when(employeeService).deleteEmployee(99L);

        mockMvc.perform(delete("/api/employees/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllDepartments_ReturnsDepartmentArray() throws Exception {
        mockMvc.perform(get("/api/employees/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(Department.values().length)));
    }
}
