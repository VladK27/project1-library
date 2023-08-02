package ru.karelin.project.services;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.karelin.project.models.Book;
import ru.karelin.project.models.Person;
import ru.karelin.project.repositories.BooksRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BooksRepository booksRepository;
    private final EntityManager entityManager;

    @Autowired
    public BookService(BooksRepository booksRepository, EntityManager entityManager) {
        this.booksRepository = booksRepository;
        this.entityManager = entityManager;
    }

    public List<Book> index(){

        return booksRepository.findAll();
    }

    public Page<Book> index(int pageNumber, int booksPerPage){

        return booksRepository.findAll(PageRequest.of(pageNumber, booksPerPage));
    }

    public Page<Book> index(int pageNumber, int booksPerPage, String sortProperty){

        return booksRepository.findAll(
                PageRequest.of(pageNumber, booksPerPage, Sort.by(sortProperty)));
    }

    public Optional<Book> show(int id){
        return booksRepository.findById(id);
    }

    //byTitle
    public Optional<Book> show(String title){
        return booksRepository.findByTitle(title);
    }

    //byOwner
    public List<Book> showByOwnerId(int ownerId){
        Session session = entityManager.unwrap(Session.class);
        Person personProxy = null;
        if(ownerId != 0){
            personProxy = session.getReference(Person.class, ownerId);
        }
        return booksRepository.findByOwner(personProxy);
    }

    @Transactional(readOnly = false)
    public void save(Book book){
        booksRepository.save(book);
    }

    @Transactional(readOnly = false)
    public void edit(Book updatedBook){
        booksRepository.save(updatedBook);
    }

    @Transactional(readOnly = false)
    public void delete(int id){
        booksRepository.deleteById(id);
    }

    @Transactional(readOnly = false)
    public void setOwner(int bookId, int personId){
        Session session = entityManager.unwrap(Session.class);

        Book book = booksRepository.findById(bookId).get();
        Person reader = null;
        if(personId != 0){
            reader = session.getReference(Person.class, personId);
            book.setDateOfIssue(new Date());
        }
        else {
            book.setDateOfIssue(null);
        }

        book.setOwner(reader);
    }

    public List<Book> find(String searchProperty, Object searchValue) {
        List<Book> books;

        switch (searchProperty) {
            case "title" -> {
                return booksRepository.findAllByTitleStartingWith((String) searchValue);
            }
            case "author" -> {
                return booksRepository.findAllByAuthorStartingWith((String) searchValue);
            }
            case "year" -> {
                return booksRepository.findAllByYear((Integer) searchValue);
            }
            default -> {
                return null;
            }
        }
    }

    @Transactional(readOnly = false)
    public void add1000Books(){
        Session session = this.entityManager.unwrap(Session.class);

        for (int i = 0; i < 1000; i++) {
            Book book = new Book();
            book.setTitle("book" + (i+1));
            book.setAuthor("Test Author");
            book.setYear(1984);
            session.persist(book);
        }
    }
}
