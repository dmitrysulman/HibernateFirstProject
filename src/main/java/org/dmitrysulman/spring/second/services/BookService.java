package org.dmitrysulman.spring.second.services;

import org.dmitrysulman.spring.second.models.Book;
import org.dmitrysulman.spring.second.models.Person;
import org.dmitrysulman.spring.second.repositories.BookRepository;
import org.dmitrysulman.spring.second.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final PersonRepository personRepository;

    @Autowired
    public BookService(BookRepository bookRepository, PersonRepository personRepository) {
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findOne(int id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public int save(Book book) {
        book = bookRepository.save(book);
        return book.getId();
    }

    @Transactional
    public void update(int id, Book book) {
        book.setId(id);
        bookRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    public Optional<Person> getBookOwner(int id) {
        Optional<Book> book = findOne(id);
        return book.map(Book::getPerson);
    }

    @Transactional
    public void assign(Book book, Person person) {
        book.setPerson(person);
        book.setDateTaken(new Date());
        bookRepository.save(book);

    }

    @Transactional
    public void release(Book book) {
        book.setPerson(null);
        book.setDateTaken(null);
        bookRepository.save(book);
    }
}
