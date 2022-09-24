package org.dmitrysulman.spring.second.services;

import org.dmitrysulman.spring.second.models.Book;
import org.dmitrysulman.spring.second.models.Person;
import org.dmitrysulman.spring.second.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Page<Book> findAll(Integer page, Integer booksPerPage, Boolean sortByYear) {
        Sort sort = Sort.unsorted();
        if (Objects.nonNull(sortByYear) && sortByYear) {
            sort = Sort.by("year");
        }
        if (Objects.isNull(page) && Objects.isNull(booksPerPage) && Objects.isNull(sortByYear)) {
            return bookRepository.findAll(Pageable.unpaged());
        }
        if (Objects.isNull(page)) {
            page = 0;
        }
        if (Objects.isNull(booksPerPage)) {
            booksPerPage = 10;
        }
        return bookRepository.findAll(PageRequest.of(page, booksPerPage, sort));
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

    public List<Book> findByTitleStartingWith(String title) {
        return bookRepository.findByTitleStartingWithIgnoreCase(title);
    }
}
