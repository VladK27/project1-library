package ru.karelin.project.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.karelin.project.models.Person;
import ru.karelin.project.services.PersonService;

@Component
public class PersonValidator implements Validator {
    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if(personService.show(person.getName(), person.getSurname()).isPresent()){
            errors.rejectValue("name", "", "This combination of name and surname already exists");
            errors.rejectValue("surname", "", "This combination of name and surname already exists");
        }
    }
}
