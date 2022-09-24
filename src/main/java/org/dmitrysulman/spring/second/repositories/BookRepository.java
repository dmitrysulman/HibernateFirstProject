package org.dmitrysulman.spring.second.repositories;

import org.dmitrysulman.spring.second.models.Book;
import org.dmitrysulman.spring.second.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByPerson(Person person);

    List<Book> findByTitleStartingWithIgnoreCase(String title);
}
