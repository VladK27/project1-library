package ru.karelin.project.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.karelin.project.models.Person;

import java.util.List;

@Component
public class PersonDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index(){
        String SQL = "SELECT * FROM Person";
        List<Person> people = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Person.class));
        System.out.println(people);
        return people;
    }

    public Person show(int id){
        String SQL = "SELECT * FROM Person WHERE id = " + id;
        Person person = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(new Person());

        return person;
    }

    public void save(Person person){
        String SQL = "INSERT INTO Person(name, surname, yearOfBirth) VALUES(?, ?, ?)";
        jdbcTemplate.update(SQL, person.getName(), person.getSurname(), person.getYearOfBirth());
    }

    public void edit(Person person){
        String SQL = "UPDATE Person SET name = ?, surname = ?, yearOfBirth = ? WHERE id = ?";
        jdbcTemplate.update(SQL, person.getName(), person.getSurname(), person.getYearOfBirth(), person.getId());
    }

    public void delete(int id){
        String SQL = "DELETE FROM Person WHERE id = " + id;
        jdbcTemplate.update(SQL);
    }
}
