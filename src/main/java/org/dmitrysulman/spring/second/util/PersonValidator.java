package org.dmitrysulman.spring.second.util;

import org.dmitrysulman.spring.second.models.Person;
import org.dmitrysulman.spring.second.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {
    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        Optional<Person> personWithSameName = personService.findByFullName(person.getFullName());
        if (personWithSameName.isPresent() && (person.getId() == 0 || person.getId() != personWithSameName.get().getId())) { //!personDAO.show(person.getId()).get().getFullName().equals(person.getFullName()))) {
            errors.rejectValue("fullName", "", "This name already exist");
        }
    }
}
