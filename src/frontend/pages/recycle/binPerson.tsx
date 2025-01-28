import { FC, useRef, useState, useEffect } from "react";
import { IPerson } from "../person/personPage";
import "./binPerson.css";
import axios from "axios";

export const PersonRecyclePage: FC = () => {
  const inputUserSearch = useRef<HTMLInputElement>(null);

  const [removedPersons, setRemovedPersons] = useState<IPerson[]>([
    { id: 34, email: "kkal@mail.ru", personType: "collague", isChecked: false },
  ]);
  const [isDelete, setIsDelete] = useState<boolean>(false);

  const toggleChecked = (id: string | number) => {
    setRemovedPersons((prevPersons) =>
      prevPersons.map((person) =>
        person.id === id ? { ...person, isChecked: !person.isChecked } : person
      )
    );
  };

  let removingPersonsId: object;

  useEffect(() => {
    const isPersonChecked = removedPersons.some((item) => item.isChecked);

    isPersonChecked ? setIsDelete(true) : setIsDelete(false);

    removingPersonsId = removedPersons
      .filter((item) => item.isChecked === true)
      .map((item) => item.id);
  }, [removedPersons]);

  const onlyFriend = () => {
    setRemovedPersons(
      removedPersons.filter((item) => item.personType === "friend")
    );
  };
  const onlyCustom = () => {
    setRemovedPersons(
      removedPersons.filter((item) => item.personType === "custom")
    );
  };
  const onlyCollague = () => {
    setRemovedPersons(
      removedPersons.filter((item) => item.personType === "collague")
    );
  };

  const onlyClient = () => {
    setRemovedPersons(
      removedPersons.filter((item) => item.personType === "client")
    );
  };

  const onlyGeneral = () => {
    setRemovedPersons(
      removedPersons.filter((item) => item.personType === "general")
    );
  };

  const onlyFamily = () => {
    setRemovedPersons(
      removedPersons.filter((item) => item.personType === "family")
    );
  };

  const allPersons = () => setRemovedPersons(removedPersons);

  const clearMarkedPersons = async () => {
    await axios.delete("http://localhost:8080/recyclebin", removingPersonsId);
  };

    const clearAllRemovedPersons = async () => {
      await axios.delete("http://localhost:8080/recyclebin/clean");
    };

    const getAllRemovedPersons = async () => {
      const response = await axios.get("http://localhost:8080/recyclebin");

      setRemovedPersons(response.data);
    };

    useEffect(() => {
      getAllRemovedPersons();
      clearAllRemovedPersons();
      clearMarkedPersons();
    }, []);

  return (
    <>
      <div className="recycle-page-title">
        <p>Recycle Bin</p>
      </div>
      <div className="recycle-container">
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
          <button
            className="clear-all-removed-persons-button"
            onClick={clearAllRemovedPersons}
          >
            Clear All
          </button>
          <input
            type="text"
            placeholder="Enter text for user search..."
            className="search-person-item-input"
            ref={inputUserSearch}
          />
          <button
            className={isDelete ? "delete-person-button" : "add-person-button"}
          >
            {isDelete ? "✖" : "+"}
          </button>
        </nav>

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
            {removedPersons.map((item, index) => (
              <div
                className={
                  index % 2 === 0
                    ? "person-info-item-black"
                    : "person-info-item"
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
                    onChange={() => toggleChecked(item.id)}
                  />
                </p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </>
  );
};
