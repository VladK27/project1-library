package ru.karelin.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.karelin.project.models.Book;
import ru.karelin.project.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findByTitle(String title);

    List<Book> findByOwner(Person owner);

    List<Book> findAllByTitleStartingWith(String title);
    List<Book> findAllByAuthorStartingWith(String title);
    List<Book> findAllByYear(Integer year);

}
