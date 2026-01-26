import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import Navbar from './Navbar';

describe('Navbar', () => {
  const mockOnAddClick = vi.fn();
  const mockOnViewChange = vi.fn();

  const defaultProps = {
    currentView: 'employees' as const,
    onViewChange: mockOnViewChange,
    onAddClick: mockOnAddClick,
  };

  it('renders the application title', () => {
    render(<Navbar {...defaultProps} />);
    expect(screen.getByText('Employee Management')).toBeInTheDocument();
  });

  it('renders add employee button when on employees view', () => {
    render(<Navbar {...defaultProps} />);
    expect(screen.getByText('+ Add Employee')).toBeInTheDocument();
  });

  it('hides add employee button when on reports view', () => {
    render(<Navbar {...defaultProps} currentView="reports" />);
    expect(screen.queryByText('+ Add Employee')).not.toBeInTheDocument();
  });

  it('renders API docs link', () => {
    render(<Navbar {...defaultProps} />);
    expect(screen.getByText('API Docs')).toBeInTheDocument();
  });

  it('renders navigation tabs', () => {
    render(<Navbar {...defaultProps} />);
    expect(screen.getByText('Employees')).toBeInTheDocument();
    expect(screen.getByText('Reports')).toBeInTheDocument();
  });

  it('calls onViewChange when clicking reports tab', () => {
    render(<Navbar {...defaultProps} />);
    fireEvent.click(screen.getByText('Reports'));
    expect(mockOnViewChange).toHaveBeenCalledWith('reports');
  });
});
