package com.demo.employee.service;

import com.demo.employee.dto.EmployeeDTO;
import com.demo.employee.exception.DuplicateEmailException;
import com.demo.employee.exception.EmployeeNotFoundException;
import com.demo.employee.model.Department;
import com.demo.employee.model.Employee;
import com.demo.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDepartment(Department.ENGINEERING);
        employee.setSalary(new BigDecimal("75000.00"));
        employee.setHireDate(LocalDate.of(2023, 1, 15));

        employeeDTO = new EmployeeDTO(
                null,
                "John",
                "Doe",
                "john.doe@example.com",
                Department.ENGINEERING,
                new BigDecimal("75000.00"),
                LocalDate.of(2023, 1, 15)
        );
    }

    @Test
    void getAllEmployees_ReturnsListOfEmployees() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee));

        List<EmployeeDTO> result = employeeService.getAllEmployees();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("john.doe@example.com");
        verify(employeeRepository).findAll();
    }

    @Test
    void getEmployeeById_WhenExists_ReturnsEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeDTO result = employeeService.getEmployeeById(1L);

        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(result.getFirstName()).isEqualTo("John");
    }

    @Test
    void getEmployeeById_WhenNotExists_ThrowsException() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getEmployeeById(99L))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void createEmployee_WhenEmailUnique_CreatesEmployee() {
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDTO result = employeeService.createEmployee(employeeDTO);

        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void createEmployee_WhenEmailExists_ThrowsException() {
        when(employeeRepository.existsByEmail("john.doe@example.com")).thenReturn(true);

        assertThatThrownBy(() -> employeeService.createEmployee(employeeDTO))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("john.doe@example.com");

        verify(employeeRepository, never()).save(any());
    }

    @Test
    void updateEmployee_WhenExists_UpdatesEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDTO updateDTO = new EmployeeDTO(
                1L, "Jane", "Doe", "john.doe@example.com",
                Department.MARKETING, new BigDecimal("80000.00"), LocalDate.of(2023, 1, 15)
        );

        EmployeeDTO result = employeeService.updateEmployee(1L, updateDTO);

        assertThat(result).isNotNull();
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void updateEmployee_WhenNotExists_ThrowsException() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.updateEmployee(99L, employeeDTO))
                .isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    void deleteEmployee_WhenExists_DeletesEmployee() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void deleteEmployee_WhenNotExists_ThrowsException() {
        when(employeeRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> employeeService.deleteEmployee(99L))
                .isInstanceOf(EmployeeNotFoundException.class);

        verify(employeeRepository, never()).deleteById(any());
    }

    @Test
    void getEmployeesByDepartment_ReturnsFilteredList() {
        when(employeeRepository.findByDepartment(Department.ENGINEERING))
                .thenReturn(Arrays.asList(employee));

        List<EmployeeDTO> result = employeeService.getEmployeesByDepartment(Department.ENGINEERING);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDepartment()).isEqualTo(Department.ENGINEERING);
    }
}
