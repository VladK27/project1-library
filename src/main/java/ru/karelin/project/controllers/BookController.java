package ru.karelin.project.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.karelin.project.dao.BookDao;
import ru.karelin.project.dao.PersonDao;
import ru.karelin.project.models.Book;
import ru.karelin.project.models.Person;
import ru.karelin.project.utility.BookValidator;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookDao bookDao;
    private final PersonDao personDao;
    private final BookValidator bookValidator;

    @Autowired
    public BookController(BookDao bookDao, PersonDao personDao, BookValidator bookValidator) {
        this.bookDao = bookDao;
        this.personDao = personDao;
        this.bookValidator = bookValidator;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("books", bookDao.index());


        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") int id) {
        if(id > bookDao.getMaxId()){
            return "pageNotFound";
        }
        Book book = bookDao.show(id);
        model.addAttribute("book", book);
        Person owner = personDao.show(book.getOwner());
        model.addAttribute("ownerName", owner.getFullName());
        model.addAttribute("ownerId", id);

        return "books/show";
    }

    @GetMapping("/new")
    public String getAddPage(Model model) {
        model.addAttribute("book", new Book());

        return "books/new";
    }


    @PostMapping("/new")
    public String save(@ModelAttribute("book") @Valid Book book, Model model, BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            return "books/new";
        } else {
            bookDao.save(book);
            return "redirect:/books";
        }
    }

    @GetMapping("/{id}/edit")
    public String getEditPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookDao.show(id));

        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String edit(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors()) {
            return "book/edit";
        }
        else {
            bookDao.edit(book);
            return "redirect:/books/{id}";
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookDao.delete(id);

        return "redirect:books";
    }

    @GetMapping("/control")
    public String getControlPage(Model model) {
        model.addAttribute("people", personDao.index());
        model.addAttribute("books", bookDao.index());

        return "books/control";
    }

    @PatchMapping("/control")
    public String setOwner(@ModelAttribute("bookId") int bookId,
                           @ModelAttribute("personId") int personId, BindingResult bindingResult) {
        if(bookDao.show(bookId).hasOwner()){
            bindingResult.rejectValue("owner", "", "The book already has an owner");
            return "redirect:/books/control";
        }
        bookDao.setOwner(bookId, personId);

        return "redirect:/books/control";
    }

}