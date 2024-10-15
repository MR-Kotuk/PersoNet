package com.mrkotuk.PersoNet.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrkotuk.PersoNet.model.Person;

@Repository
public interface PersonRepo extends JpaRepository<Person, Integer> {
}
