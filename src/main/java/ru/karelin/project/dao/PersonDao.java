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
        List<Person> people = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>());

        return people;
    }

    public Person show(int id){
        String SQl = String.format("SELECT * FROM Person WHERE id = %d", id);
        Person person = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>());

        return person;
    }

    public

}
