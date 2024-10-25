package com.mrkotuk.PersoNet.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mrkotuk.PersoNet.components.PersonType;
import com.mrkotuk.PersoNet.model.Person;

@Repository
public interface PersonRepo extends JpaRepository<Person, Integer> {
    public List<Person> findByUsername(String username);

    @Query("SELECT p.personType FROM Person p WHERE p.username = :username")
    public List<PersonType> findPersonTypesByUsername(@Param("username") String username);
}
