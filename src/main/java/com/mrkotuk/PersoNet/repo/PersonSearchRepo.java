package com.mrkotuk.PersoNet.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mrkotuk.PersoNet.components.PersonStatus;
import com.mrkotuk.PersoNet.components.PersonType;
import com.mrkotuk.PersoNet.domain.model.Person;

public interface PersonSearchRepo extends JpaRepository<Person, Integer> {
    @Query("SELECT DISTINCT p FROM Person p " +
            "JOIN p.lineTemplates lt " +
            "WHERE p.email = :email " +
            "AND (:keyword IS NULL OR LOWER(lt.lineValue) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:status IS NULL OR p.personStatus IN :status) " +
            "AND (:types IS NULL OR p.personType IN :types) ")
    public List<Person> findByFilters(
            @Param("email") String email,
            @Param("keyword") String keyword,
            @Param("status") Set<PersonStatus> status,
            @Param("types") Set<PersonType> types);

    @Query("SELECT p.tags FROM Person p WHERE p.email = :email")
    public Set<String> findTagsByEmail(@Param("email") String email);

    @Query("SELECT p.personType FROM Person p WHERE p.email = :email")
    public Set<PersonType> findTypesByEmail(@Param("email") String email);

    @Query("SELECT p.personStatus FROM Person p WHERE p.email = :email")
    public Set<PersonStatus> findStatusesByEmail(@Param("email") String email);
}