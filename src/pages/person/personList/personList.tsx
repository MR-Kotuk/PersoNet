import { FC, useState } from "react";
import "./personList.css";

export const PersonList: FC = () => {
  interface IPerson {
    id: number | string;
    personType: "private" | "public";
    firstName: string;
    lastName: string;
  }

  const [persons, setPersons] = useState<IPerson[]>([
    {
      id: 759,
      personType: "private",
      firstName: "Ivan",
      lastName: "Pronin",
    },

    {
      id: 993,
      personType: "public",
      firstName: "Stepan",
      lastName: "lol",
    },

    {
      id: 6546,
      personType: "private",
      firstName: "Anton",
      lastName: "Gondon",
    },

    {
      id: 7239,
      personType: "public",
      firstName: "Ivan",
      lastName: "Eblan",
    },
  ]);

  return (
    <>
      <div className="person-table-wrapper">
        <div className="person-table-titles">
          <p className="person-tabel-title-name" id="person-table-title-id">
            PersonId
          </p>
          <p
            className="person-tabel-title-name"
            id="person-table-title-persontype"
          >
            PersonType
          </p>
          <p
            className="person-tabel-title-name"
            id="person-table-title-firstname"
          >
            FirstName
          </p>
          <p
            className="person-tabel-title-name"
            id="person-table-title-lastname"
          >
            LastName
          </p>
          <p
            className="person-tabel-title-name"
            id="person-table-title-selectcircle"
          >
            SelectCircle
          </p>
        </div>

        <div className="person-info">
          {persons.map((item, index) => (
            <div
              className={
                index % 2 === 0 ? "person-info-item-black" : "person-info-item"
              }
              key={index}
            >
              <p>{item.id}</p>
              <p>{item.personType}</p>
              <p>{item.firstName}</p>
              <p>{item.lastName}</p>
              <p>
                <input type="checkbox" />
              </p>
            </div>
          ))}
        </div>
      </div>
    </>
  );
};
