import { ChangeEvent, FC, FormEvent, useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import "./auth.css";

export const AuthPage: FC = () => {
  interface IAuthData {
    email: string;
    password: string;
  }

  const [authData, setAuthData] = useState<IAuthData>({
    email: "",
    password: "",
  });

  const setAuthDataInput = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    setAuthData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const postAuthData = async (e: FormEvent) => {
    e.preventDefault();

    try {
      const response = await axios.post(
        "http://localhost:8080/auth/login",
        authData
      );

      const token = response.data;
      localStorage.setItem("token", token);
      window.location.href = "/home";
    } catch (e) {
      alert("Invalid data");
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-greeting-wrapper">
        <p className="auth-greeting-title">
          <span>Welcome to</span> Perso<span>|||</span>et
        </p>

        <p className="auth-greeting-text">
          The best way to manage and organize <br />
          your contacts effortlessly
        </p>
      </div>
      <div className="auth-field-wrapper">
        <p className="auth-field-title">
          Perso<span>|||</span>et
        </p>

        <p className="auth-field-text">Log in</p>

        <form className="auth-field" onSubmit={postAuthData}>
          <div className="auth-field-item">
            <p>Email</p>
            <input
              type="email"
              name="email"
              placeholder="Enter your email"
              onChange={setAuthDataInput}
            />
          </div>

          <div className="auth-field-item">
            <p>Password</p>
            <input
              type="password"
              placeholder="Enter your password"
              onChange={setAuthDataInput}
            />
          </div>

          <Link to="/" className="auth-forgot-password-item">
            Forgot password?
          </Link>

          <div className="auth-field-item">
            <button className="auth-submit" type="submit">
              Submit
            </button>

            <p className="auth-enter-account">
              Have no account?{" "}
              <Link to="/register" className="auth-enter-account-link">
                Create
              </Link>
            </p>
          </div>
        </form>
      </div>
    </div>
  );
};
