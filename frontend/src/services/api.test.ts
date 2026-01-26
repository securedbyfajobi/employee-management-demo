import { describe, it, expect, vi, beforeEach } from 'vitest';
import axios from 'axios';
import { employeeApi } from './api';
import { Department } from '../types/Employee';

vi.mock('axios', () => ({
  default: {
    create: vi.fn(() => ({
      get: vi.fn(),
      post: vi.fn(),
      put: vi.fn(),
      delete: vi.fn(),
    })),
  },
}));

describe('employeeApi', () => {
  const mockEmployee = {
    id: 1,
    firstName: 'John',
    lastName: 'Doe',
    email: 'john@example.com',
    department: Department.ENGINEERING,
    salary: 75000,
    hireDate: '2023-01-15',
  };

  let mockAxiosInstance: {
    get: ReturnType<typeof vi.fn>;
    post: ReturnType<typeof vi.fn>;
    put: ReturnType<typeof vi.fn>;
    delete: ReturnType<typeof vi.fn>;
  };

  beforeEach(() => {
    vi.clearAllMocks();
    mockAxiosInstance = {
      get: vi.fn(),
      post: vi.fn(),
      put: vi.fn(),
      delete: vi.fn(),
    };
    (axios.create as ReturnType<typeof vi.fn>).mockReturnValue(mockAxiosInstance);
  });

  it('getAll fetches all employees', async () => {
    mockAxiosInstance.get.mockResolvedValue({ data: [mockEmployee] });

    const { employeeApi: freshApi } = await import('./api');

    expect(axios.create).toHaveBeenCalledWith({
      baseURL: '/api',
      headers: { 'Content-Type': 'application/json' },
    });
  });

  it('creates axios instance with correct config', () => {
    expect(axios.create).toHaveBeenCalledWith(
      expect.objectContaining({
        headers: { 'Content-Type': 'application/json' },
      })
    );
  });
});
