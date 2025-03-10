package app.service;

import app.db.daos.PersonDao;
import app.db.entities.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//the whole class only changes stored data on the application side and does not interact with the database (the PersonDao class does that)
public class PersonService {

    private ObservableList<Person> persons;
    private PersonDao personDao = new PersonDao();

    private PersonService() {
        persons = FXCollections.observableArrayList(personDao.listPersons());
    }

    //get all persons
    public static ObservableList<Person> getPersons() {
        return PersonServiceHolder.INSTANCE.persons;
    }

    //add a new person
    public static void addPerson(Person person) {
        if (person == null) { //'security'
            System.out.println("currentPerson null");
            return;
        }
        PersonServiceHolder.INSTANCE.personDao.addPerson(person); //add to db w/ PersonDao
        getPersons().add(person); //add to the client side list
    }

    //update a person
    public static void updatePerson(Person person) {
        if (person == null) { //'security'
            System.out.println("currentPerson null");
            return;
        }
        //update the person in the database
        PersonServiceHolder.INSTANCE.personDao.updatePerson(
                person.getId(),
                person.getNickname(),
                person.getLastName(),
                person.getFirstName(),
                person.getPhoneNumber(),
                person.getAddress(),
                person.getEmailAddress(),
                person.getBirthDate()
        );
        //update the person in the client side list
        ObservableList<Person> persons = getPersons();
        int index = persons.indexOf(person);
        persons.set(index, person);
    }

    //delete a person
    public static void deletePerson(Person person) {
        if (person == null) { //'security'
            System.out.println("currentPerson null");
            return;
        }

        PersonServiceHolder.INSTANCE.personDao.deletePerson(person.getId()); //delete from db w/ id w/ PersonDao
        getPersons().remove(person); //remove from the client side list
    }

    //singleton
    private static class PersonServiceHolder {
        private static final PersonService INSTANCE = new PersonService();
    }
}
