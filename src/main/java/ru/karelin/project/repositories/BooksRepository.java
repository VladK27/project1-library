package ru.karelin.project.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<Book> findAllByOwner(Person person, Pageable pageable);

    Page<Book> findAllByTitleStartingWith(String title, Pageable pageable);
    Page<Book> findAllByAuthorStartingWith(String author, Pageable pageable);
    Page<Book> findAllByYear(Integer year, Pageable pageable);
}
