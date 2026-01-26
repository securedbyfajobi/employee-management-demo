import { describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import Navbar from './Navbar';

describe('Navbar', () => {
  it('renders the application title', () => {
    render(<Navbar />);
    expect(screen.getByText('Employee Management')).toBeInTheDocument();
  });

  it('renders navigation links', () => {
    render(<Navbar />);
    expect(screen.getByText('Employees')).toBeInTheDocument();
  });
});
