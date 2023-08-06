package ru.karelin.project.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.karelin.project.models.Book;
import ru.karelin.project.models.Person;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByNameAndSurname(String name, String surname);

    Page<Person> findAllByNameStartingWith(String name, Pageable pageable);
    Page<Person> findAllBySurnameStartingWith(String surname, Pageable pageable);
    Page<Person> findAllByYearOfBirth(Integer yearOfBirth, Pageable pageable);
}
