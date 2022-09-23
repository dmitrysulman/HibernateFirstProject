package org.dmitrysulman.spring.second.services;

import org.dmitrysulman.spring.second.models.Book;
import org.dmitrysulman.spring.second.models.Person;
import org.dmitrysulman.spring.second.repositories.BookRepository;
import org.dmitrysulman.spring.second.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;
    private final BookRepository bookRepository;

    @Autowired
    public PersonService(PersonRepository personRepository, BookRepository bookRepository) {
        this.personRepository = personRepository;
        this.bookRepository = bookRepository;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Optional<Person> findOne(int id) {
        return personRepository.findById(id);
    }

    public Optional<Person> findByFullName(String fullName) {
        return personRepository.findByFullName(fullName);
    }

    @Transactional
    public int save(Person person) {
        person = personRepository.save(person);
        return person.getId();
    }

    @Transactional
    public void update(int id, Person person) {
        person.setId(id);
        personRepository.save(person);
    }

    @Transactional
    public void delete(int id) {
        personRepository.deleteById(id);
    }

    public List<Book> getBooksOfPerson(Person person) {
        return bookRepository.findByPerson(person);
    }
}
