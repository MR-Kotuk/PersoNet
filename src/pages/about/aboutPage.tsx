import { FC } from "react";
import "./aboutPage.css";

export const AboutPage: FC = () => {
  return (
    <>
      <div className="about-page-container">
        <nav className="about-nav-container">
          <div className="about-nav-container-item">
            <span className="slash">/</span>
            <p>/Security/</p>
            <span className="slash">/</span>
          </div>
          <div className="about-nav-container-item">
            <span className="slash">/</span>
            <p>/Simplicity/</p>
            <span className="slash">/</span>
          </div>
          <div className="about-nav-container-item">
            <span className="slash">/</span>
            <p>/Customization/</p>
            <span className="slash">/</span>
          </div>
          <div className="about-nav-container-item">
            <span className="slash">/</span>
            <p>/Variety/</p>
            <span className="slash">/</span>
          </div>
          <div className="about-nav-container-item">
            <span className="slash">/</span>
            <p>/Private Personas/</p>
            <span className="slash">/</span>
          </div>
          <div className="about-nav-container-item">
            <span className="slash">/</span>
            <p>/Public Personas/</p>
            <span className="slash">/</span>
          </div>
          <div className="about-nav-container-item">
            <span className="slash">/</span>
            <p>/Notifications/</p>
            <span className="slash">/</span>
          </div>
          <div className="about-nav-container-item">
            <span className="slash">/</span>
            <p>/Support/</p>
            <span className="slash">/</span>
          </div>
        </nav>
      </div>
    </>
  );
};
