package ru.karelin.project.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.karelin.project.models.Book;
import ru.karelin.project.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class BookDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index(){
        String SQL = "SELECT * FROM Book";
        List<Book> books = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Book.class));

        return books;
    }

    public Book show(int id){
        String SQL = "SELECT * FROM Book WHERE id = " + id;
        Book book = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(new Book());

        return book;
    }

    public Book showByOwner(int ownerId){
        String SQL = "SELECT * FROM Book WHERE owner = " + ownerId;
        Book book = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(new Book());

        return book;
    }

    public void save(Book book){
        String SQL = "INSERT INTO Book(title, author, year) VALUES(?, ?, ?)";
        jdbcTemplate.update(SQL, book.getTitle(), book.getAuthor(), book.getYear());
    }

    public void edit(Book book){
        String SQL = "UPDATE Book SET title = ?, author = ?, year = ? WHERE id = ?";
        jdbcTemplate.update(SQL, book.getTitle(), book.getAuthor(), book.getYear(), book.getId());
    }

    public void delete(int id){
        String SQL = "DELETE FROM Book WHERE id = " + id;
        jdbcTemplate.update(SQL);
    }

    public void setOwner(int bookId, int personId){
        String SQL = "UPDATE Book SET owner = ? WHERE id = ?";
        jdbcTemplate.update(SQL, personId, bookId);
    }

    public Optional<Person> show(String title) {
        String SQL = "SELECT * FROM Book WHERE title = ?";

        return jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Person.class), title)
                .stream().findAny();
    }
}