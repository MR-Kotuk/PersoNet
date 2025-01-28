import { FC, useEffect, useRef, useState } from "react";
import "./personPage.css";
import { PersonList } from "./personList/personList";
import axios from "axios";
import { useNavigate } from "react-router-dom";

export interface IPerson {
  id: number | string;
  email: string;
  personType:
    | "friend"
    | "custom"
    | "collague"
    | "client"
    | "general"
    | "family";
  isChecked?: boolean;
}

export const PersonPage: FC = () => {
  const inputUserSearch = useRef<HTMLInputElement>(null);

  const navigate = useNavigate();

  const [persons, setPersons] = useState<IPerson[]>([
    {
      id: 1,
      email: "lalala@mail.com",
      personType: "friend",
      isChecked: false,
    },
    {
      id: 54,
      email: "azaza@mail.com",
      personType: "custom",
      isChecked: false,
    },
    {
      id: 792,
      email: "kek@mail.com",
      personType: "collague",
      isChecked: false,
    },

    { id: 192, email: "bob@mail.com", personType: "client", isChecked: false },
    { id: 2014, email: "db@mail.com", personType: "general", isChecked: false },
  ]);
  const [isDelete, setIsDelete] = useState<boolean>(false);

  useEffect(() => {
    const isPersonChecked = persons.some((item) => item.isChecked);

    isPersonChecked ? setIsDelete(true) : setIsDelete(false);

    removingPersonsId = persons
      .filter((item) => item.isChecked === true)
      .map((item) => item.id);
  }, [persons]);

  const getAllPersons = async () => {
    try {
      const getPersonResponse = await axios.get<IPerson[]>(
        "http://localhost:8080/person"
      );

      setPersons(getPersonResponse.data);
    } catch (e) {
      alert(e);
    }
  };

  let removingPersonsId: object;

  const removeMarkedPersons = async () => {
    try {
      await axios.delete("http://localhost:8080/person/", removingPersonsId);
    } catch (e) {
      alert(e);
    }
  };

  useEffect(() => {
    getAllPersons();
  }, []);

  useEffect(() => {
    const isPersonChecked = persons.some((item) => item.isChecked);

    isPersonChecked ? setIsDelete(true) : setIsDelete(false);

    removingPersonsId = persons
      .filter((item) => item.isChecked === true)
      .map((item) => item.id);
  }, [persons]);

  const toggleChecked = (id: string | number) => {
    setPersons((prevPersons) =>
      prevPersons.map((person) =>
        person.id === id ? { ...person, isChecked: !person.isChecked } : person
      )
    );
  };

  const onlyFriend = () => {
    setPersons(persons.filter((item) => item.personType === "friend"));
  };
  const onlyCustom = () => {
    setPersons(persons.filter((item) => item.personType === "custom"));
  };
  const onlyCollague = () => {
    setPersons(persons.filter((item) => item.personType === "collague"));
  };

  const onlyClient = () => {
    setPersons(persons.filter((item) => item.personType === "client"));
  };

  const onlyGeneral = () => {
    setPersons(persons.filter((item) => item.personType === "general"));
  };

  const onlyFamily = () => {
    setPersons(persons.filter((item) => item.personType === "family"));
  };

  const allPersons = () => setPersons(persons);

  return (
    <>
      <div className="person-container">
        <nav className="select-person-wrapper">
          <button className="select-person-button" onClick={allPersons}>
            All persons
          </button>
          <button className="select-person-button" onClick={onlyFriend}>
            Friends
          </button>
          <button className="select-person-button" onClick={onlyCustom}>
            Customs
          </button>
          <button className="select-person-button" onClick={onlyCollague}>
            Collagues
          </button>
          <button className="select-person-button" onClick={onlyClient}>
            Clients
          </button>
          <button className="select-person-button" onClick={onlyGeneral}>
            Generals
          </button>
          <button className="select-person-button" onClick={onlyFamily}>
            Relatives
          </button>
          <input
            type="text"
            placeholder="Enter text for user search..."
            className="search-person-item-input"
            ref={inputUserSearch}
          />
          <button
            className={isDelete ? "delete-person-button" : "add-person-button"}
            onClick={() => (isDelete ? removeMarkedPersons : navigate("*"))}
          >
            {isDelete ? "✖" : "+"}
          </button>
        </nav>
      </div>

      <PersonList persons={persons} toggleChecked={toggleChecked} />
    </>
  );
};
