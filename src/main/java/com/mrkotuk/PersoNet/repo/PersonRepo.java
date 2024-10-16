package com.mrkotuk.PersoNet.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrkotuk.PersoNet.model.Person;

@Repository
public interface PersonRepo extends JpaRepository<Person, Integer> {
    public List<Person> findByUsername(String username);
}
