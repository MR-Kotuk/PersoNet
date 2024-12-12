package com.mrkotuk.PersoNet.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mrkotuk.PersoNet.components.PersonStatus;
import com.mrkotuk.PersoNet.model.Person;

@Repository
public interface PersonSenderRepo extends JpaRepository<Person, Integer> {
        @Query("SELECT DISTINCT p FROM Person p " +
                        "JOIN p.lineTemplates lt " +
                        "WHERE p.personStatus = :personStatus " +
                        "AND p.email = :email " +
                        "AND lt.lineName = 'Email' " +
                        "AND lt.lineValue IS NOT NULL")
        public List<Person> findByEmailAndStatusAndValidLineTemplate(
                        @Param("email") String email,
                        @Param("personStatus") PersonStatus personStatus);

        @Query("SELECT lt.lineName " +
                        "FROM Person p " +
                        "JOIN p.lineTemplates lt " +
                        "WHERE p.personId IN :personIds " +
                        "GROUP BY lt.lineName " +
                        "HAVING COUNT(DISTINCT p.personId) = :personCount")
        public List<String> findSharedLineTemplateNamesByPersonId(
                        @Param("personIds") List<Integer> personIds,
                        @Param("personCount") int personCount);
}