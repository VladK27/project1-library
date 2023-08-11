package ru.karelin.project.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.karelin.project.models.Book;
import ru.karelin.project.models.Person;
import ru.karelin.project.services.BookService;
import ru.karelin.project.services.PersonService;
import ru.karelin.project.utility.PageConfig;
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

    @GetMapping()
    public String index(Model model,
                        @RequestParam(required = false, defaultValue = "1", name = "page") int pageNumber,
                        @RequestParam(required = false, defaultValue = "10", name = "books_per_page") int booksPerPage,
                        @RequestParam(required = false, name = "sort_by") String sortProperty){
        Page<Person> peoplePage;

        //sorting
        if(sortProperty != null){
            if(Person.propertiesSet.contains(sortProperty)){
                peoplePage = personService.index(pageNumber-1, booksPerPage, sortProperty);
            }
            else{
                System.out.println("\u001B[31m Wrong sort property: " + sortProperty);
                peoplePage = personService.index(pageNumber - 1, booksPerPage);
            }
        }
        else{
            peoplePage = personService.index(pageNumber - 1, booksPerPage);
        }

        //pagination
        model.addAttribute("people", peoplePage.getContent());

        if(!(peoplePage.getTotalPages() <= 1)){
            PageConfig pageConfig = new PageConfig(pageNumber, peoplePage.getTotalPages());
            model.addAttribute("pageConfig", pageConfig);
        }

        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(
            Model model,
            @PathVariable("id") int id,
            @RequestParam(required = false, defaultValue = "1", name = "page") int pageNumber
    ){
        Optional<Person> personOptional = personService.show(id);

        if(personOptional.isEmpty()){
            return "errors/404";
        }

        Page<Book> booksPage = bookService.show(id, pageNumber-1);
        List<Book> books = booksPage.getContent();

        if(id != 0){
            for(var book : books){
                book.setOverdue();
            }
        }

        model.addAttribute("person", personOptional.get());
        model.addAttribute("books", books);

        if(!(booksPage.getTotalPages() <= 1)){
            PageConfig pageConfig = new PageConfig(pageNumber, booksPage.getTotalPages());
            model.addAttribute("pageConfig", pageConfig);
        }


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
        Optional<Person> personOptional = personService.show(id);

        if(personOptional.isEmpty()){
            return "errors/404";
        }

        model.addAttribute("person", personOptional.get());
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

    @GetMapping("/search")
    public String search(Model model,
                         @RequestParam( required = false, name = "search_value") String searchValue,
                         @RequestParam( required = false, name = "search_by") String searchProperty,
                         @RequestParam( required = false, name = "page", defaultValue = "1") Integer pageNumber )
    {
        if(searchValue != null){
            Page<Person> peoplePage = personService.find(searchProperty, searchValue, pageNumber-1);

            List<Person> people = peoplePage.getContent();
            model.addAttribute("people", people);

            if(people.isEmpty()){
                return "people/search";
            }

            if(!(peoplePage.getTotalPages() <= 1)){
                PageConfig pageConfig = new PageConfig(pageNumber, peoplePage.getTotalPages());
                model.addAttribute("pageConfig", pageConfig);
            }

        }

        return "people/search";
    }
}
