import { FC, useState } from "react";
import "./personList.css";
import { IPerson } from "../personPage";

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
            PersonPos
          </p>
          <p
            className="person-tabel-title-name"
            id="person-table-title-persontype"
          >
            PersonId
          </p>
          <p
            className="person-tabel-title-name"
            id="person-table-title-firstname"
          >
            PersonType
          </p>
          <p
            className="person-tabel-title-name"
            id="person-table-title-lastname"
          >
            Email
          </p>
          <p
            className="person-tabel-title-name"
            id="person-table-title-selectcircle"
          >
            Select
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
              <p>{index + 1}</p>
              <p>{item.id}</p>
              <p>{item.personType}</p>
              <p>{item.email}</p>

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
