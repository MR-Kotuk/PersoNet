import { FC } from "react";
import "./register.css";
import { Link } from "react-router-dom";

export const RegisterPage: FC = () => {
  return (
    <div className="register-container">
      <div className="register-greeting-wrapper">
        <p className="register-greeting-title">
          <span>Welcome to</span> Perso <span>|||</span> et
        </p>

        <p className="register-greeting-text">
          The best way to manage and organize <br />
          your contacts effortlessly
        </p>
      </div>
      <div className="register-field-wrapper">
        <p className="register-field-title">
          Perso <span>|||</span> et
        </p>

        <p className="register-field-text">Create account</p>

        <div className="register-field">
          <div className="register-field-item">
            <p>Username</p>
            <input type="text" placeholder="Enter your name" />
          </div>

          <div className="register-field-item">
            <p>Email</p>
            <input type="email" placeholder="Enter your email" />
          </div>

          <div className="register-field-item">
            <p>Password</p>
            <input type="password" placeholder="Enter your password" />
          </div>

          <div className="register-field-item">
            <p>Confirm password</p>
            <input type="password" placeholder="Confirm your password" />
          </div>

          <div className="register-field-item">
            <button className="register-submit">Submit</button>
            <p className="register-enter-account">
              already have an account?{" "}
              <Link to="*" className="register-enter-account-link">
                Login
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};
