import { FC, useRef } from "react";
import "./personPage.css";
import { PersonList } from "./personList/personList";

export const PersonPage: FC = () => {
  const inputUserSearch = useRef<HTMLInputElement>(null);

  return (
    <>
      <div className="person-container">
        <nav className="select-person-wrapper">
          <button className="select-person-wrapper-item">All persons</button>
          <button className="select-person-wrapper-item">Private</button>
          <button className="select-person-wrapper-item">Public</button>
          <input
            type="text"
            placeholder="Enter text for user search..."
            className="search-person-item-input"
            ref={inputUserSearch}
          />
        </nav>
      </div>

      <PersonList />
    </>
  );
};
