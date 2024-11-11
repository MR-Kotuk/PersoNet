import "./header.css";
import { NavLink } from "react-router-dom";

export const Header = () => {
  return (
    <div className="header-container">
      <nav className="header-wrapper">
        <ul className="header-wrap-items">
          <li className="wrap-item">
            <NavLink
              to="/"
              className={({ isActive }) =>
                isActive ? "wrap-item-active-link" : "wrap-item-passive-link"
              }
            >
              Home
            </NavLink>
          </li>
          <li className="wrap-item">
            <NavLink
              to="/drgrhtfh"
              className={({ isActive }) =>
                isActive ? "wrap-item-active-link" : "wrap-item-passive-link"
              }
            >
              About
            </NavLink>
          </li>
          <li className="wrap-item">
            <NavLink
              className={({ isActive }) =>
                isActive ? "wrap-item-active-link" : "wrap-item-passive-link"
              }
              to="/"
            >
              Persons
            </NavLink>
          </li>
          <li className="wrap-item">
            <NavLink
              to="/"
              className={({ isActive }) =>
                isActive ? "wrap-item-active-link" : "wrap-item-passive-link"
              }
            >
              Account
            </NavLink>
          </li>
          <li className="wrap-item">
            <NavLink
              to="/"
              className={({ isActive }) =>
                isActive ? "wrap-item-active-link" : "wrap-item-passive-link"
              }
            >
              Settings
            </NavLink>
          </li>
        </ul>
      </nav>
    </div>
  );
};
