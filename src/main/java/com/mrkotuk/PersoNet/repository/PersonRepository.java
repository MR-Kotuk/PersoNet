package com.mrkotuk.PersoNet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mrkotuk.PersoNet.domain.enums.PersonStatus;
import com.mrkotuk.PersoNet.domain.enums.PersonType;
import com.mrkotuk.PersoNet.domain.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    @Query("SELECT p.personType FROM Person p WHERE p.personStatus = :personStatus AND p.email = :email")
    List<PersonType> findPersonTypesByStatusAndEmail(
            @Param("personStatus") PersonStatus personStatus,
            @Param("email") String email);

    @Query("SELECT p FROM Person p WHERE p.personStatus = :personStatus AND p.email = :email")
    List<Person> findByStatusAndEmail(
            @Param("personStatus") PersonStatus personStatus,
            @Param("email") String email);

    @Query("SELECT p FROM Person p WHERE p.personStatus = :personStatus AND p.personId = :personId")
    Optional<Person> findByStatusAndId(
            @Param("personStatus") PersonStatus personStatus,
            @Param("personId") Integer personId);
}
