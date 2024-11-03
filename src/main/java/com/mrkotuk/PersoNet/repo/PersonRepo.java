package com.mrkotuk.PersoNet.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mrkotuk.PersoNet.components.PersonStatus;
import com.mrkotuk.PersoNet.components.PersonType;
import com.mrkotuk.PersoNet.model.Person;

@Repository
public interface PersonRepo extends JpaRepository<Person, Integer> {
    @Query("SELECT p.personType FROM Person p WHERE p.personStatus = :personStatus AND p.username = :username")
    public List<PersonType> findPersonTypesByStatusAndUsername(
            @Param("personStatus") PersonStatus personStatus,
            @Param("username") String username);

    @Query("SELECT p FROM Person p WHERE p.personStatus = :personStatus AND p.username = :username")
    public List<Person> findByStatusAndUsername(
            @Param("personStatus") PersonStatus personStatus,
            @Param("username") String username);

    @Query("SELECT p FROM Person p WHERE p.personStatus = :personStatus AND p.personId = :personId")
    public Optional<Person> findByStatusAndId(
            @Param("personStatus") PersonStatus personStatus,
            @Param("personId") Integer personId);

    @Query("SELECT p FROM Person p JOIN p.lineTemplates lt " +
            "WHERE LOWER(lt.lineValue) LIKE LOWER(CONCAT('%', :lineValue, '%')) " +
            "AND p.username = :username " +
            "AND p.personStatus = :personStatus")
    public List<Person> findByUsernameAndStatusAndLineValue(
            @Param("username") String username,
            @Param("personStatus") PersonStatus personStatus,
            @Param("lineValue") String lineValue);
}