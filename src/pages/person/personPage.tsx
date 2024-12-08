import { FC, useRef, useState } from "react";
import "./personPage.css";
import { PersonList } from "./personList/personList";

export const PersonPage: FC = () => {
  const inputUserSearch = useRef<HTMLInputElement>(null);

  interface IPerson {
    id: number | string;
    personType: "private" | "public";
    firstName: string;
    lastName: string;
    isChecked: boolean;
  }

  const [persons, setPersons] = useState<IPerson[]>([
    {
      id: 759,
      personType: "private",
      firstName: "Ivan",
      lastName: "Pronin",
      isChecked: false,
    },

    {
      id: 993,
      personType: "public",
      firstName: "Stepan",
      lastName: "lol",
      isChecked: false,
    },

    {
      id: 6546,
      personType: "private",
      firstName: "Anton",
      lastName: "Gondon",
      isChecked: false,
    },
  ]);

  const toggleChecked = (id: string | number) => {
    setPersons((prevPersons) =>
      prevPersons.map((person) =>
        person.id === id ? { ...person, isChecked: !person.isChecked } : person
      )
    );
  };

  const onlyPrivate = () =>
    setPersons(persons.filter((item) => item.personType === "private"));

  const onlyPublic = () =>
    setPersons(persons.filter((item) => item.personType === "public"));

  const allPersons = () => setPersons(persons);

  return (
    <>
      <div className="person-container">
        <nav className="select-person-wrapper">
          <button className="select-person-wrapper-item" onClick={allPersons}>
            All persons
          </button>
          <button className="select-person-wrapper-item" onClick={onlyPrivate}>
            Private
          </button>
          <button className="select-person-wrapper-item" onClick={onlyPublic}>
            Public
          </button>
          <input
            type="text"
            placeholder="Enter text for user search..."
            className="search-person-item-input"
            ref={inputUserSearch}
          />
        </nav>
      </div>

      <PersonList persons={persons} toggleChecked={toggleChecked} />
    </>
  );
};
