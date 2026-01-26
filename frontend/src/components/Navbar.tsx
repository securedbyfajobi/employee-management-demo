export type ViewType = 'employees' | 'reports';

interface NavbarProps {
  currentView: ViewType;
  onViewChange: (view: ViewType) => void;
  onAddClick: () => void;
}

function Navbar({ currentView, onViewChange, onAddClick }: NavbarProps) {
  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <h1>Employee Management</h1>
        <div className="nav-tabs">
          <button
            className={`nav-tab ${currentView === 'employees' ? 'active' : ''}`}
            onClick={() => onViewChange('employees')}
          >
            Employees
          </button>
          <button
            className={`nav-tab ${currentView === 'reports' ? 'active' : ''}`}
            onClick={() => onViewChange('reports')}
          >
            Reports
          </button>
        </div>
      </div>
      <div className="navbar-actions">
        {currentView === 'employees' && (
          <button className="btn btn-primary" onClick={onAddClick}>
            + Add Employee
          </button>
        )}
        <a
          href="/swagger-ui.html"
          target="_blank"
          rel="noopener noreferrer"
          className="btn btn-secondary"
        >
          API Docs
        </a>
      </div>
    </nav>
  );
}

export default Navbar;
