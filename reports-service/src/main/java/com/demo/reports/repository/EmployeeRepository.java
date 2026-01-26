package com.demo.reports.repository;

import com.demo.reports.model.Department;
import com.demo.reports.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Long countByDepartment(Department department);

    @Query("SELECT e FROM Employee e WHERE e.hireDate >= :startDate ORDER BY e.hireDate DESC")
    List<Employee> findNewHiresSince(@Param("startDate") LocalDate startDate);

    @Query("SELECT e FROM Employee e WHERE e.hireDate BETWEEN :startDate AND :endDate ORDER BY e.hireDate DESC")
    List<Employee> findHiresBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<Employee> findByDepartment(Department department);

    @Query("SELECT e.salary FROM Employee e ORDER BY e.salary")
    List<java.math.BigDecimal> findAllSalariesOrdered();
}
