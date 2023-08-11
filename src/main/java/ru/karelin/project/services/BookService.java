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

    //by id
    public Optional<Book> show(int id){
        return booksRepository.findById(id);
    }

    //by title
    public Optional<Book> show(String title){
        return booksRepository.findByTitle(title);
    }

    //by owner id
    public List<Book> showByOwnerId(int ownerId){
        //if ownerId == null - book in library; Owner == null
        if(ownerId == 0){
            return booksRepository.findByOwner(null);
        }

        Session session = entityManager.unwrap(Session.class);
        Person personProxy = session.getReference(Person.class, ownerId);

        return booksRepository.findByOwner(personProxy);
    }

    public Page<Book> show(Integer ownerId, int pageNumber){
        if(ownerId == 0){
            return booksRepository.findAllByOwner(null, PageRequest.of(pageNumber, 10));
        }

        Session session = entityManager.unwrap(Session.class);
        Person ownerProxy = session.getReference(Person.class, ownerId);
        return booksRepository.findAllByOwner(ownerProxy, PageRequest.of(pageNumber, 10));
    }

    @Transactional()
    public void save(Book book){
        booksRepository.save(book);
    }

    @Transactional()
    public void edit(Book updatedBook){
        booksRepository.save(updatedBook);
    }

    @Transactional()
    public void delete(int id){
        booksRepository.deleteById(id);
    }

    @Transactional()
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

    public Page<Book> find(String searchProperty, String searchValue, Integer pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, 10);

        switch (searchProperty) {
            case "title" -> {
                return booksRepository.findAllByTitleStartingWith(searchValue, pageRequest);
            }
            case "author" -> {
                return booksRepository.findAllByAuthorStartingWith(searchValue, pageRequest);
            }
            case "year" -> {
                try{
                    return booksRepository.findAllByYear(Integer.valueOf(searchValue), pageRequest);
                }
                catch (NumberFormatException e){
                    return Page.empty();
                }
            }
            default -> {
                return Page.empty();
            }
        }
    }
}
