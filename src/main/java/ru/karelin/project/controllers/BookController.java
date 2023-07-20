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
import ru.karelin.project.utility.BookValidator;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookValidator bookValidator;
    private final BookService bookService;
    private final PersonService personService;

    @Autowired
    public BookController(BookService bookService, PersonService personService, BookValidator bookValidator) {
        this.bookService = bookService;
        this.personService = personService;
        this.bookValidator = bookValidator;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("books", bookService.index());

        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") int id) {
        Optional<Book> bookOptional = bookService.show(id);

        if(bookOptional.isEmpty()){
            return "pageNotFound";
        }

        Book book = bookOptional.get();
        model.addAttribute("book", book);

        Person owner = book.getOwner();

        if(owner != null){
            model.addAttribute("ownerName", owner.getFullName());
            model.addAttribute("ownerId", owner.getId());
        }

        return "books/show";
    }

    @GetMapping("/new")
    public String getAddPage(Model model) {
        model.addAttribute("book", new Book());

        return "books/new";
    }


    @PostMapping("/new")
    public String save(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors()) {
            return "books/new";
        } else {
            bookService.save(book);
            return "redirect:/books";
        }
    }

    @GetMapping("/{id}/edit")
    public String getEditPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.show(id).get());

        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String edit(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "book/edit";
        }
        else {
            bookService.edit(book);
            return "redirect:/books/{id}";
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);

        return "redirect:/books";
    }

    @GetMapping("/control")
    public String getControlPage(Model model) {
        model.addAttribute("people", personService.index());
        model.addAttribute("books", bookService.index());

        return "books/control";
    }

    @PatchMapping("/control")
    public String setOwner(@ModelAttribute("bookId") int bookId,
                           @ModelAttribute("personId") int personId, Model model) {

        Book book = bookService.show(bookId).get();
        Person reader = personService.show(personId).get();

        if(book.hasOwner() && personId != 0) {
            model.addAttribute("ownerError", String.format(
                    "The book \"%s\" already has an owner - %s",
                     book.getTitle(), book.getOwner().getFullName()
            ));

            return getControlPage(model);
        }
        bookService.setOwner(bookId, personId);

        String successMessage = null;
        if(personId == 0){
            successMessage = String.format(
                    "The book \"%s\" has been successfully returned to library",
                    book.getTitle()
            );
        }
        else{
            successMessage = String.format(
                    "The book \"%s\" has been successfully issued to the %s",
                    book.getTitle(), reader.getFullName()
            );
        }
        model.addAttribute("successMessage",successMessage);

        return getControlPage(model);
    }

}