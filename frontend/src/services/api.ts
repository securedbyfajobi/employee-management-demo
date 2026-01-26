import axios from 'axios';
import { Employee, Department } from '../types/Employee';
import { HeadcountReport, SalarySummary, DepartmentStats, NewHire } from '../types/Reports';

const API_BASE_URL = import.meta.env.VITE_API_URL || '/api';
const REPORTS_API_URL = import.meta.env.VITE_REPORTS_API_URL || '/api/reports';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

const reportsApi = axios.create({
  baseURL: REPORTS_API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const reportsApiService = {
  getHeadcount: async (): Promise<HeadcountReport> => {
    const response = await reportsApi.get<HeadcountReport>('/headcount');
    return response.data;
  },

  getSalarySummary: async (): Promise<SalarySummary> => {
    const response = await reportsApi.get<SalarySummary>('/salary');
    return response.data;
  },

  getDepartmentStats: async (): Promise<DepartmentStats[]> => {
    const response = await reportsApi.get<DepartmentStats[]>('/departments');
    return response.data;
  },

  getNewHires: async (days: number = 30): Promise<NewHire[]> => {
    const response = await reportsApi.get<NewHire[]>(`/new-hires?days=${days}`);
    return response.data;
  },
};

export const employeeApi = {
  getAll: async (): Promise<Employee[]> => {
    const response = await api.get<Employee[]>('/employees');
    return response.data;
  },

  getById: async (id: number): Promise<Employee> => {
    const response = await api.get<Employee>(`/employees/${id}`);
    return response.data;
  },

  getByDepartment: async (department: Department): Promise<Employee[]> => {
    const response = await api.get<Employee[]>(`/employees/department/${department}`);
    return response.data;
  },

  create: async (employee: Omit<Employee, 'id'>): Promise<Employee> => {
    const response = await api.post<Employee>('/employees', employee);
    return response.data;
  },

  update: async (id: number, employee: Omit<Employee, 'id'>): Promise<Employee> => {
    const response = await api.put<Employee>(`/employees/${id}`, employee);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/employees/${id}`);
  },
};

export default api;
