import { ChangeEvent, FC, FormEvent, useState } from "react";
import "./register.css";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";

export const RegisterPage: FC = () => {
  interface IRegisterData {
    username: string;
    email: string;
    password: string;
    confirmPassword?: string;
  }

  const [regData, setRegData] = useState<IRegisterData>({
    username: "",
    email: "",
    password: "",
  });

  const navigate = useNavigate();

  const setRegisterDataInput = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    setRegData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const postRegisterData = async (e: FormEvent) => {
    e.preventDefault();

    try {
      const response = await axios.post(
        "http://localhost:8080/auth/register",
        regData
      );

      console.log(response);
    } catch (e) {
      alert(e);
    }
  };

  return (
    <div className="register-container">
      <div className="register-greeting-wrapper">
        <p className="register-greeting-title">
          <span>Welcome to</span> Perso<span>|||</span>et
        </p>

        <p className="register-greeting-text">
          The best way to manage and organize <br />
          your contacts effortlessly
        </p>
      </div>
      <div className="register-field-wrapper">
        <p className="register-field-title">
          Perso<span>|||</span>et
        </p>

        <p className="register-field-text">Create account</p>

        <form className="register-field" onSubmit={postRegisterData}>
          <div className="register-field-item">
            <p>Username</p>
            <input
              type="text"
              name="username"
              placeholder="Enter your name"
              onChange={setRegisterDataInput}
            />
          </div>

          <div className="register-field-item">
            <p>Email</p>
            <input
              type="email"
              name="email"
              placeholder="Enter your email"
              onChange={setRegisterDataInput}
            />
          </div>

          <div className="register-field-item">
            <p>Password</p>
            <input
              type="password"
              name="password"
              placeholder="Enter your password"
              onChange={setRegisterDataInput}
            />
          </div>

          <div className="register-field-item">
            <p>Confirm password</p>
            <input type="password" placeholder="Confirm your password" />
          </div>

          <div className="register-field-item">
            <button className="register-submit" type="submit">
              Submit
            </button>

            <div className="oauth-buttons-field">
              <a
                className="google-auth-link"
                href="http://localhost:8080/oauth2/authorization/google"
              >
                <button className="google-auth-button">
                  Sign up with Google
                </button>
              </a>

              <a
                className="github-auth-link"
                href="http://localhost:8080/oauth2/authorization/github"
              >
                <button className="github-auth-button">
                  Sign up with Github
                </button>
              </a>
            </div>

            <p className="register-enter-account">
              Already have an account?{" "}
              <Link to="/auth" className="register-enter-account-link">
                Login
              </Link>
            </p>
          </div>
        </form>
      </div>
    </div>
  );
};
