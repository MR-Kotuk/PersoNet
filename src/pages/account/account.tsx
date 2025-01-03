import axios from "axios";
import { FC, useEffect, useState, useRef } from "react";
import "./account.css";

export const AccountPage: FC = () => {
  const changeUsernameInput = useRef<HTMLInputElement>(null);

  interface IAccountData {
    id: number;
    username: string;
    email: string;
    password: string;
    verified: boolean;
  }

  const [accountData, setAccountData] = useState<IAccountData>();

  const [password, setPassword] = useState<string>();
  const [newPassword, setNewPassword] = useState<string>();

  const getAccountData = async () => {
    try {
      const response = await axios.get("http://localhost:8080/account/");

      setAccountData(response.data);
    } catch (e) {
      console.log(e);
    }
  };

  const changeUsernameData = async () => {
    try {
      await axios.put(
        "http://localhost:8080/account/set-username",
        changeUsernameInput.current?.value
      );
    } catch (e) {
      console.log(e);
    }
  };

  const changePasswordData = async () => {
    try {
      await axios.put("http://localhost:8080/account/set-password", {
        password,
        newPassword,
      });
    } catch (e) {
      console.log(e);
    }
  };

  useEffect(() => {
    getAccountData();
  }, []);

  return (
    <div className="account-page-container">
      <h1 className="profile-title">Profile</h1>
      <div className="account-interface-field">
        <div className="account-data-field">
          <h1 className="account-data-title">Account Data</h1>

          <p className="account-data-item">Username: {accountData?.username}</p>
          <p className="account-data-item">Email: {accountData?.email}</p>
          <p className="account-data-item">Password: {accountData?.password}</p>
          <p className="account-data-item">
            Is Verify: {accountData?.verified ? "Yes" : "No"}
          </p>
        </div>

        <div className="account-field-options">
          <div className="account-change-data-item">
            <p>Change Username</p>
            <input
              type="text"
              placeholder="Change your username"
              ref={changeUsernameInput}
            />
            <button onClick={changeUsernameData}>Accept</button>
          </div>

          <div className="account-change-data-item">
            <p>Change Password</p>
            <input
              type="text"
              placeholder="Enter your valid password"
              onChange={(e) => setPassword(e.target.value)}
            />
            <input
              type="text"
              placeholder="Enter your new password"
              onChange={(e) => setNewPassword(e.target.value)}
            />
            <button onClick={changePasswordData}>Accept</button>
          </div>
        </div>
      </div>
    </div>
  );
};
