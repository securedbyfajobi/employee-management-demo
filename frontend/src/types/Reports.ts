import { Department } from './Employee';

export interface HeadcountReport {
  totalEmployees: number;
  byDepartment: Record<Department, number>;
  newHiresThisMonth: number;
  newHiresThisYear: number;
}

export interface SalarySummary {
  totalEmployees: number;
  totalPayroll: number;
  averageSalary: number;
  minSalary: number;
  maxSalary: number;
  medianSalary: number;
}

export interface DepartmentStats {
  department: Department;
  employeeCount: number;
  totalSalary: number;
  averageSalary: number;
  minSalary: number;
  maxSalary: number;
}

export interface NewHire {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  department: Department;
  hireDate: string;
  daysSinceHire: number;
}
