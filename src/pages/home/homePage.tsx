import { FC } from "react";
import "./homePage.css";
import { Link } from "react-router-dom";

export const HomePage: FC = () => {
  let token: string | undefined;

  return (
    <>
      <div className="layout-container">
        <p>
          <span>Welcome to</span> Perso <span>|||</span> et{" "}
          {!token ? "" : undefined}!
        </p>

        <span id="follow-to-auth-link">
          For log in, follow to <Link to="*">link</Link>
        </span>

        <p className="analytic-contacts-title">
          <span id="slash">/</span> Your contacts analytic
          <span id="slash"> /</span>
        </p>

        <div className="contact-data-wrap"></div>
      </div>
    </>
  );
};
