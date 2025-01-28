import "./header.css";
import { NavLink } from "react-router-dom";
import axios from "axios";
import { useEffect, useState } from "react";

export const Header = () => {
  const [name, setName] = useState<string>("");
  let token = localStorage.getItem("token");

  const getName = async () => {
    try {
      const name = await axios.get("http://localhost:8080/");

      setName(name.data);
    } catch (e) {
      console.log(e);
    }
  };

  useEffect(() => {
    getName();
  });

  return (
    <div className="header-container">
      <nav className="header-wrapper">
        <ul className="header-wrap-items">
          <li className="wrap-item">
            <NavLink
              to="/home"
              className={({ isActive }) =>
                isActive ? "wrap-item-active-link" : "wrap-item-passive-link"
              }
            >
              Home
            </NavLink>
          </li>
          <li className="wrap-item">
            <NavLink
              to="/about"
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
              to="/person"
            >
              Persons
            </NavLink>
          </li>
          <li className="wrap-item">
            <NavLink
              to="/account"
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

        <p className="profile-follow-wrapper">{token ? name : "Log in"}</p>
      </nav>
    </div>
  );
};
