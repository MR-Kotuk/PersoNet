import { ChangeEvent, FC, useState } from "react";
import "./register.css";
import { Link } from "react-router-dom";
import { GoogleLogin } from "@react-oauth/google";

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

  const setRegisterDataInput = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    setRegData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

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

        <form className="register-field">
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
            <button className="register-submit">Submit</button>
            <GoogleLogin
              onSuccess={(credentialResponse) => {
                console.log(credentialResponse);
              }}
              onError={() => {
                console.log("Login Failed");
              }}
            />
            ;
            <p className="register-enter-account">
              Already have an account?{" "}
              <Link to="*" className="register-enter-account-link">
                Login
              </Link>
            </p>
          </div>
        </form>
      </div>
    </div>
  );
};
