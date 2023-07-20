package ru.karelin.project.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.karelin.project.models.Book;
import ru.karelin.project.models.Person;
import ru.karelin.project.services.BookService;
import ru.karelin.project.services.PersonService;
import ru.karelin.project.utility.PersonValidator;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/people")
public class PersonController {

    private final PersonValidator personValidator;
    private final PersonService personService;
    private final BookService bookService;

    @Autowired
    public PersonController(PersonService personService, BookService bookService, PersonValidator personValidator) {
        this.personService = personService;
        this.bookService = bookService;
        this.personValidator = personValidator;
    }

    //Get all people from dao and bring to view
    @GetMapping()
    public String index(Model model){
        model.addAttribute("people", personService.index());

        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") int id){
        Optional<Person> personOptional = personService.show(id);

        if(personOptional.isEmpty()){
            return "pageNotFound";
        }

        model.addAttribute("person", personOptional.get());
        model.addAttribute("books", bookService.showByOwnerId(id));

        return "people/show";
    }

    @GetMapping("/new")
    public String getPageNew(Model model){
        model.addAttribute("person", new Person());

        return "people/new";
    }

    @PostMapping("/new")
    public String save(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        personValidator.validate(person, bindingResult);

        if(bindingResult.hasErrors()){
            return "people/new";
        }
        personService.save(person);

        return "redirect:/people";
    }

    @GetMapping("{id}/edit")
    public String getPageEdit(Model model, @PathVariable("id") int id){
        model.addAttribute("person", personService.show(id).get());

        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String edit(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "people/edit";
        }

        personService.edit(person);
        return "redirect:/people/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        personService.delete(id);
        return "redirect:/people";
    }

}
