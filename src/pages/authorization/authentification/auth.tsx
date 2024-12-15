import { FC } from "react";
import { Link } from "react-router-dom";
import "./auth.css";

export const AuthPage: FC = () => {
  return (
    <div className="auth-container">
      <div className="auth-greeting-wrapper">
        <p className="auth-greeting-title">
          <span>Welcome to</span> Perso <span>|||</span> et
        </p>

        <p className="auth-greeting-text">
          The best way to manage and organize <br />
          your contacts effortlessly
        </p>
      </div>
      <div className="auth-field-wrapper">
        <p className="auth-field-title">
          Perso <span>|||</span> et
        </p>

        <p className="auth-field-text">Create account</p>

        <form className="auth-field">
          <div className="auth-field-item">
            <p>Email</p>
            <input type="email" name="email" placeholder="Enter your email" />
          </div>

          <div className="auth-field-item">
            <p>Password</p>
            <input type="password" placeholder="Enter your password" />
          </div>

          <p className="auth-forgot-password-item">Forgot password?</p>

          <div className="auth-field-item">
            <button className="auth-submit">Submit</button>

            <p className="auth-enter-account">
              Already have no account?{" "}
              <Link to="*" className="auth-enter-account-link">
                Create
              </Link>
            </p>
          </div>
        </form>
      </div>
    </div>
  );
};
