package ru.karelin.project.services;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.karelin.project.models.Book;
import ru.karelin.project.models.Person;
import ru.karelin.project.repositories.PeopleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final static Person LIBRARY = new Person(0, "Library", "storage", 2006);

    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> index(){
        return peopleRepository.findAll();
    }

    public Optional<Person> show(int id){

        if(id == 0){
            return Optional.of(LIBRARY);
        }

        Optional<Person> optionalPerson = peopleRepository.findById(id);
        return optionalPerson;
    }

    @Transactional(readOnly = false)
    public void save(Person person){
        peopleRepository.save(person);
    }

    @Transactional(readOnly = false)
    public void edit(Person person){
        peopleRepository.save(person);
    }

    @Transactional(readOnly = false)
    public void delete(int id){
        peopleRepository.deleteById(id);
    }

    public Optional<Person> show(String name, String surname) {
        return peopleRepository.findByNameAndSurname(name, surname);
    }
}