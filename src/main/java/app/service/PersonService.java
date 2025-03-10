package app.service;

import app.db.daos.PersonDao;
import app.db.entities.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PersonService {

    private ObservableList<Person> persons;
    private PersonDao personDao = new PersonDao(); //to use db

    private PersonService() {
        //System.out.println("PersonService start..."); //debug
        persons = FXCollections.observableArrayList();
        personDao.listPersons().forEach(person -> persons.add(person));
        //System.out.println("PersonService over."); //debug
    }

    public static ObservableList<Person> getPersons() {
        return PersonServiceHolder.INSTANCE.persons;
    }

    public static void addPerson(Person person) {
        PersonService.PersonServiceHolder.INSTANCE.persons.add(person);
    }

    private static class PersonServiceHolder {
        private static final PersonService INSTANCE = new PersonService();
    }
}
