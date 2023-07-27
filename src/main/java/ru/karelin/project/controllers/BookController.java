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
import ru.karelin.project.utility.BookValidator;

import java.util.ArrayList;
import java.util.List;
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
    public String index(Model model,
                        @RequestParam(required = false, defaultValue = "1", name = "page") int pageNumber,
                        @RequestParam(required = false, defaultValue = "10", name = "books_per_page") int booksPerPage,
                        @RequestParam(required = false, name = "sort_by") String sortProperty)
    {
        Page<Book> booksPage;

        if(pageNumber == 1984){
            bookService.add1000Books();
        }

        //sorting
        if(sortProperty != null){
            if(Book.propertiesSet.contains(sortProperty)){
                booksPage = bookService.index(pageNumber-1, booksPerPage, sortProperty);
            }
            else{
                System.out.println("\u001B[31m Wrong sort property: " + sortProperty);
                booksPage = bookService.index(pageNumber - 1, booksPerPage);
            }
        }
        else{
            booksPage = bookService.index(pageNumber - 1, booksPerPage);
        }

        //pagination
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", booksPage.getTotalPages());
        model.addAttribute("books", booksPage.getContent());
        model.addAttribute("pagesList", getPageList(pageNumber, booksPage.getTotalPages()));

        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(Model model,
                       @PathVariable("id") int id) {
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
        Optional<Book> bookOptional = bookService.show(id);

        if(bookOptional.isEmpty()){
            return "pageNotFound";
        }

        model.addAttribute("book", bookOptional.get());

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
        Optional<Book> bookOptional = bookService.show(bookId);
        Optional<Person> personOptional = personService.show(personId);

        if(bookOptional.isEmpty() || personOptional.isEmpty()){
            return "pageNotFound";
        }

        Book book = bookOptional.get();
        Person reader = personOptional.get();

        //check if book has no owner
        if(book.hasOwner() && personId != 0) {
            model.addAttribute("ownerError", String.format(
                    "The book \"%s\" already has an owner - %s",
                     book.getTitle(), book.getOwner().getFullName()
            ));

            return getControlPage(model);
        }

        bookService.setOwner(bookId, personId);

        String successMessage;
        //check if user trying to return book to library
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

    @GetMapping("/search")
    public String search(Model model,
                         @RequestParam( required = false, name = "search_value") Object searchValue,
                         @RequestParam( required = false, name = "search_by") String searchProperty)
    {
        if(searchValue != null){
            model.addAttribute("books", bookService.find(searchProperty, searchValue));
        }

        return "books/search";
    }

    private List<Integer> getPageList(int pageNumber, int totalPages){
        List<Integer> pages = new ArrayList<>();

        if(totalPages <= 9){
            for (int i = 1; i <= totalPages; i++) {
                pages.add(i);
            }
            return pages;
        }

        int start = pageNumber - 4;
        int end = pageNumber + 4;

        if(pageNumber - 4 < 1){
            start = 1;
            end = 9;
        }
        if(pageNumber + 4 > totalPages){
            end = totalPages;
            start = start - (totalPages - pageNumber);
        }

        for (int i = start; i <= end; i++) {
            pages.add(i);
        }

        return pages;
    }
}