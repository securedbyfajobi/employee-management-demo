import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import EmployeeForm from './EmployeeForm';
import { Department } from '../types/Employee';

describe('EmployeeForm', () => {
  const mockOnSubmit = vi.fn();
  const mockOnCancel = vi.fn();

  const defaultProps = {
    onSubmit: mockOnSubmit,
    onCancel: mockOnCancel,
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders empty form for new employee', () => {
    render(<EmployeeForm {...defaultProps} />);

    expect(screen.getByLabelText(/first name/i)).toHaveValue('');
    expect(screen.getByLabelText(/last name/i)).toHaveValue('');
    expect(screen.getByLabelText(/email/i)).toHaveValue('');
  });

  it('renders form with employee data when editing', () => {
    const employee = {
      id: 1,
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      department: Department.ENGINEERING,
      salary: 75000,
      hireDate: '2023-01-15',
    };

    render(<EmployeeForm {...defaultProps} employee={employee} />);

    expect(screen.getByLabelText(/first name/i)).toHaveValue('John');
    expect(screen.getByLabelText(/last name/i)).toHaveValue('Doe');
    expect(screen.getByLabelText(/email/i)).toHaveValue('john@example.com');
  });

  it('calls onCancel when cancel button is clicked', () => {
    render(<EmployeeForm {...defaultProps} />);

    fireEvent.click(screen.getByText(/cancel/i));

    expect(mockOnCancel).toHaveBeenCalledTimes(1);
  });

  it('renders all department options', () => {
    render(<EmployeeForm {...defaultProps} />);

    const departmentSelect = screen.getByLabelText(/department/i);
    expect(departmentSelect).toBeInTheDocument();

    Object.values(Department).forEach(dept => {
      expect(screen.getByRole('option', { name: dept })).toBeInTheDocument();
    });
  });

  it('shows correct button text for new vs edit mode', () => {
    const { rerender } = render(<EmployeeForm {...defaultProps} />);
    expect(screen.getByRole('button', { name: /add employee/i })).toBeInTheDocument();

    rerender(
      <EmployeeForm
        {...defaultProps}
        employee={{
          id: 1,
          firstName: 'John',
          lastName: 'Doe',
          email: 'john@example.com',
          department: Department.ENGINEERING,
          salary: 75000,
          hireDate: '2023-01-15',
        }}
      />
    );
    expect(screen.getByRole('button', { name: /update employee/i })).toBeInTheDocument();
  });
});
