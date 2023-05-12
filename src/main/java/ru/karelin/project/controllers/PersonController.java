package ru.karelin.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.karelin.project.dao.BookDao;
import ru.karelin.project.dao.PersonDao;
import ru.karelin.project.models.Person;

@Controller
@RequestMapping("/people")
public class PersonController {
    private final PersonDao personDao;
    private final BookDao bookDao;

    @Autowired
    public PersonController(PersonDao personDao, BookDao bookDao) {
        this.personDao = personDao;
        this.bookDao = bookDao;
    }

    //Get all people from dao and bring to view
    @GetMapping()
    public String index(Model model){
        model.addAttribute("people", personDao.index());

        return "people/index";
    }

    //Get one person from dao and show all information
    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id")int id){
        model.addAttribute("person", personDao.show(id));

        model.addAttribute("books", bookDao.showByOwner(id));

        return "people/show";
    }

    @GetMapping("/new")
    public String addPage(Model model){
        model.addAttribute("person", new Person());
        return "people/new";
    }

    @PostMapping("/new")
    public String save(@ModelAttribute("person") Person person){
        personDao.save(person);
        return "redirect:people";
    }

    @GetMapping("{id}/edit")
    public String editPage(Model model, @RequestParam("id") int id){
        model.addAttribute("person", personDao.show(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String edit(@ModelAttribute("person") Person person){
        personDao.edit(person);
        return "redirect:people/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(@RequestParam("id") int id){
        personDao.delete(id);
        return "redirect:people";
    }

}
