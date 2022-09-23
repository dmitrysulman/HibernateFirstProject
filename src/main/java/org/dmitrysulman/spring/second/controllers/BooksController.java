package org.dmitrysulman.spring.second.controllers;

import org.dmitrysulman.spring.second.models.Book;
import org.dmitrysulman.spring.second.models.Person;
import org.dmitrysulman.spring.second.services.BookService;
import org.dmitrysulman.spring.second.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookService bookService;
    private final PersonService personService;

    @Autowired
    public BooksController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("books", bookService.findAll());

        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        Optional<Book> book = bookService.findOne(id);
        if (book.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        } else {
            Optional<Person> bookOwner = bookService.getBookOwner(id);
            if (bookOwner.isPresent()) {
                model.addAttribute("owner", bookOwner.get());
            } else {
                model.addAttribute("people", personService.findAll());
            }
            model.addAttribute("book", book.get());
        }

        return "books/show";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("book") Book book) {
        return "books/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/add";
        }
        int id = bookService.save(book);

        return "redirect:/books/" + id;
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        Optional<Book> book = bookService.findOne(id);
        if (book.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        } else {
            model.addAttribute("book", book.get());
        }

        return "books/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }
        bookService.update(id, book);

        return "redirect:/books/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        Optional<Book> book = bookService.findOne(id);
        if (book.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        } else {
            bookService.delete(id);
        }

        return "redirect:/books/";
    }

    @PostMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        Optional<Book> book = bookService.findOne(id);
        int personId = person.getId();
        if (book.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        } else {
            Optional<Person> personToAssign = personService.findOne(personId);
            if (personToAssign.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
            } else {
                bookService.assign(book.get(), personToAssign.get());
            }
        }

        return "redirect:/books/" + id;
    }

    @PostMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        Optional<Book> book = bookService.findOne(id);
        if (book.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        } else {
            bookService.release(book.get());
        }

        return "redirect:/books/" + id;
    }
}
