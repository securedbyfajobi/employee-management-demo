import { useState, useEffect } from 'react';
import { reportsApiService } from '../services/api';
import { HeadcountReport, SalarySummary, DepartmentStats, NewHire } from '../types/Reports';
import { formatDepartment } from '../types/Employee';

function Reports() {
  const [headcount, setHeadcount] = useState<HeadcountReport | null>(null);
  const [salary, setSalary] = useState<SalarySummary | null>(null);
  const [departments, setDepartments] = useState<DepartmentStats[]>([]);
  const [newHires, setNewHires] = useState<NewHire[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchReports = async () => {
      try {
        setLoading(true);
        const [headcountData, salaryData, deptData, hiresData] = await Promise.all([
          reportsApiService.getHeadcount(),
          reportsApiService.getSalarySummary(),
          reportsApiService.getDepartmentStats(),
          reportsApiService.getNewHires(30),
        ]);
        setHeadcount(headcountData);
        setSalary(salaryData);
        setDepartments(deptData);
        setNewHires(hiresData);
        setError(null);
      } catch (err) {
        setError('Failed to fetch reports. Please try again.');
        console.error('Error fetching reports:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchReports();
  }, []);

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0,
    }).format(amount);
  };

  if (loading) {
    return (
      <div className="reports-loading">
        <div className="loading-spinner"></div>
        <p>Loading reports...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="reports-error">
        <p>{error}</p>
        <button onClick={() => window.location.reload()}>Retry</button>
      </div>
    );
  }

  return (
    <div className="reports-container">
      <h2>Reports Dashboard</h2>

      <div className="reports-grid">
        {/* Headcount Summary */}
        <div className="report-card">
          <h3>Headcount Summary</h3>
          <div className="stat-grid">
            <div className="stat-item">
              <span className="stat-value">{headcount?.totalEmployees || 0}</span>
              <span className="stat-label">Total Employees</span>
            </div>
            <div className="stat-item">
              <span className="stat-value">{headcount?.newHiresThisMonth || 0}</span>
              <span className="stat-label">New This Month</span>
            </div>
            <div className="stat-item">
              <span className="stat-value">{headcount?.newHiresThisYear || 0}</span>
              <span className="stat-label">New This Year</span>
            </div>
          </div>
        </div>

        {/* Salary Summary */}
        <div className="report-card">
          <h3>Salary Overview</h3>
          <div className="stat-grid">
            <div className="stat-item">
              <span className="stat-value">{formatCurrency(salary?.totalPayroll || 0)}</span>
              <span className="stat-label">Total Payroll</span>
            </div>
            <div className="stat-item">
              <span className="stat-value">{formatCurrency(salary?.averageSalary || 0)}</span>
              <span className="stat-label">Average Salary</span>
            </div>
            <div className="stat-item">
              <span className="stat-value">{formatCurrency(salary?.medianSalary || 0)}</span>
              <span className="stat-label">Median Salary</span>
            </div>
          </div>
          <div className="salary-range">
            <span>Range: {formatCurrency(salary?.minSalary || 0)} - {formatCurrency(salary?.maxSalary || 0)}</span>
          </div>
        </div>
      </div>

      {/* Department Stats Table */}
      <div className="report-card full-width">
        <h3>Department Statistics</h3>
        {departments.length > 0 ? (
          <table className="reports-table">
            <thead>
              <tr>
                <th>Department</th>
                <th>Employees</th>
                <th>Total Salary</th>
                <th>Avg Salary</th>
                <th>Min</th>
                <th>Max</th>
              </tr>
            </thead>
            <tbody>
              {departments.map((dept) => (
                <tr key={dept.department}>
                  <td>{formatDepartment(dept.department)}</td>
                  <td>{dept.employeeCount}</td>
                  <td>{formatCurrency(dept.totalSalary)}</td>
                  <td>{formatCurrency(dept.averageSalary)}</td>
                  <td>{formatCurrency(dept.minSalary)}</td>
                  <td>{formatCurrency(dept.maxSalary)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <p className="no-data">No department data available</p>
        )}
      </div>

      {/* New Hires */}
      <div className="report-card full-width">
        <h3>Recent Hires (Last 30 Days)</h3>
        {newHires.length > 0 ? (
          <table className="reports-table">
            <thead>
              <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Department</th>
                <th>Hire Date</th>
                <th>Days Ago</th>
              </tr>
            </thead>
            <tbody>
              {newHires.map((hire) => (
                <tr key={hire.id}>
                  <td>{hire.firstName} {hire.lastName}</td>
                  <td>{hire.email}</td>
                  <td>{formatDepartment(hire.department)}</td>
                  <td>{new Date(hire.hireDate).toLocaleDateString()}</td>
                  <td>{hire.daysSinceHire}</td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <p className="no-data">No new hires in the last 30 days</p>
        )}
      </div>
    </div>
  );
}

export default Reports;
