import { FC, useState } from "react";
import "./personList.css";

interface IPerson {
  id: number | string;
  personType: "private" | "public";
  firstName: string;
  lastName: string;
  isChecked: boolean;
}

interface PersonListProps {
  persons: IPerson[];
  toggleChecked: (id: string | number) => void;
}

export const PersonList: FC<PersonListProps> = ({ persons, toggleChecked }) => {
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
                <input
                  type="checkbox"
                  checked={item.isChecked}
                  onClick={() => {
                    toggleChecked(item.id);
                    console.log(item);
                  }}
                />
              </p>
            </div>
          ))}
        </div>
      </div>
    </>
  );
};
