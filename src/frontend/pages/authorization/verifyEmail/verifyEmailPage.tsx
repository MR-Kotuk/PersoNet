import { FC, useRef } from "react";
import "./verifyEmailPage.css";
import axios from "axios";

export const VerifyEmailPage: FC = () => {
  const emailCodeInput = useRef<HTMLInputElement>(null);

  const submitEmailCode = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8080/auth/verify-email/${emailCodeInput.current?.value}`
      );

      const token = response.data;

      if (emailCodeInput.current?.value.length === 4) {
        localStorage.setItem("token", token);
        window.location.href = "/home";
      } else {
        return;
      }
    } catch (e) {
      console.log(e);
    }
  };

  return (
    <div className="verify-email-field">
      <h2>Please check your email</h2>
      <p>
        We have sent the confirmation code to your email. Please enter
        <br />
        it in the field below
      </p>
      <input type="text" maxLength={4} ref={emailCodeInput} />
      <br />
      <button onClick={submitEmailCode}>Submit</button>
    </div>
  );
};
