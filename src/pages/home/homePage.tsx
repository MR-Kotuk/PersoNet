import { FC, useEffect, useState } from "react";
import "./homePage.css";
import { Link } from "react-router-dom";
import axios from "axios";

export const HomePage: FC = () => {
  const [greeting, setGreeting] = useState<string>("");

  const getGreeting = async () => {
    try {
      const response = await axios.get("http://localhost:8080/");

      setGreeting(response.data);
    } catch (e) {
      console.log(e);
    }
  };

  useEffect(() => {
    getGreeting();
  }, []);

  return (
    <>
      <div className="layout-container">
        <p>
          <span>Welcome {greeting} to</span> Perso<span>|||</span>
          et!
        </p>

        <span id="follow-to-auth-link">
          For log in, follow to{" "}
          <Link className="follow-to-auth-link-item" to="*">
            link
          </Link>
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
