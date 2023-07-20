package ru.karelin.project.services;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.karelin.project.models.Book;
import ru.karelin.project.models.Person;
import ru.karelin.project.repositories.BooksRepository;

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
        List<Book> books = booksRepository.findAll();

        return books;
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
        }

        book.setOwner(reader);
    }
}
