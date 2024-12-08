import { FC, useEffect, useRef, useState } from "react";
import "./personPage.css";
import { PersonList } from "./personList/personList";
import axios from "axios";

export interface IPerson {
  id: number | string;
  email: string;
  personType: "private" | "public";
  isChecked?: boolean;
}

export const PersonPage: FC = () => {
  const inputUserSearch = useRef<HTMLInputElement>(null);

  const [persons, setPersons] = useState<IPerson[]>([]);

  // const getAllPersons = async () => {
  //   try {
  //     const getPersonResponse = await axios.get<IPerson[]>(
  //       "http://localhost:8080/person/"
  //     );

  //     setPersons(getPersonResponse.data);
  //   } catch (e) {
  //     alert(e);
  //   }
  // };

  // useEffect(() => {
  //   getAllPersons();
  // }, []);

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
