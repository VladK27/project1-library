package ru.karelin.project.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.karelin.project.dao.BookDao;
import ru.karelin.project.dao.PersonDao;
import ru.karelin.project.models.Person;
import ru.karelin.project.utility.PersonValidator;

@Controller
@RequestMapping("/people")
public class PersonController {
    private final PersonDao personDao;
    private final BookDao bookDao;
    private final PersonValidator personValidator;

    @Autowired
    public PersonController(PersonDao personDao, BookDao bookDao, PersonValidator personValidator) {
        this.personDao = personDao;
        this.bookDao = bookDao;
        this.personValidator = personValidator;
    }

    //Get all people from dao and bring to view
    @GetMapping()
    public String index(Model model){
        model.addAttribute("people", personDao.index());

        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") int id){
        Person person = personDao.show(id);

        if(person.getName() == null){
            return "pageNotFound";
        }

        model.addAttribute("person", person);
        model.addAttribute("books", bookDao.showByOwner(id));

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
        personDao.save(person);

        return "redirect:/people";
    }

    @GetMapping("{id}/edit")
    public String getPageEdit(Model model, @PathVariable("id") int id){
        model.addAttribute("person", personDao.show(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String edit(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "people/edit";
        }

        personDao.edit(person);
        return "redirect:/people/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        personDao.delete(id);
        return "redirect:/people";
    }

}
