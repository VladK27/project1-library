package ru.karelin.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.karelin.project.models.Person;
import ru.karelin.project.repositories.PeopleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final static Person LIBRARY = new Person(0, "Library", "storage");

    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> index(){
        return peopleRepository.findAll();
    }

    public Page<Person> index(int pageNumber, int booksPerPage){

        return peopleRepository.findAll(PageRequest.of(pageNumber, booksPerPage));
    }

    public Page<Person> index(int pageNumber, int booksPerPage, String sortProperty){

        return peopleRepository.findAll(
                PageRequest.of(pageNumber, booksPerPage, Sort.by(sortProperty)));
    }

    public Optional<Person> show(Integer id){

        if(id == 0){
            return Optional.of(LIBRARY);
        }

        return peopleRepository.findById(id);
    }

    public Page<Person> find(String searchProperty, String searchValue, Integer pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, 10);

        switch (searchProperty) {
            case "name" -> {
                return peopleRepository.findAllByNameStartingWith(searchValue, pageRequest);
            }
            case "surname" -> {
                return peopleRepository.findAllBySurnameStartingWith(searchValue, pageRequest);
            }
            case "year_of_birth" -> {
                try{
                    return peopleRepository.findAllByYearOfBirth(Integer.valueOf(searchValue), pageRequest);
                }catch (NumberFormatException e){
                    return Page.empty();
                }
            }
            default -> {
                return Page.empty();
            }
        }
    }

    @Transactional()
    public void save(Person person){
        peopleRepository.save(person);
    }

    @Transactional()
    public void edit(Person person){
        peopleRepository.save(person);
    }

    @Transactional()
    public void delete(int id){
        peopleRepository.deleteById(id);
    }

    public Optional<Person> show(String name, String surname) {
        return peopleRepository.findByNameAndSurname(name, surname);
    }


}