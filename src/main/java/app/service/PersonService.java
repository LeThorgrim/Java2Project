package app.service;

import app.db.daos.DatabaseManager;
import app.db.daos.PersonDao;
import app.db.entities.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    //update a person in the list and in the db
    public static void updatePerson(Person currentPerson) {
        if (currentPerson == null) {
            System.out.println("currentPerson null"); //debug
            return;
        }
        //personsList update
        ObservableList<Person> persons = getPersons();
        persons.set(currentPerson.getId()-1, currentPerson); // Mise à jour de l'objet dans la liste

        // db update
        try {
            String query = "UPDATE person SET lastName=?, firstName=?, nickname=?, phone_number=?, address=?, email_address=?, birth_date=? WHERE idperson=?";
            PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(query);
            stmt.setString(1, currentPerson.getLastName());
            stmt.setString(2, currentPerson.getFirstName());
            stmt.setString(3, currentPerson.getNickname());
            stmt.setString(4, currentPerson.getPhoneNumber());
            stmt.setString(5, currentPerson.getAddress());
            stmt.setString(6, currentPerson.getEmailAddress());
            stmt.setString(7, currentPerson.getBirthDate());
            stmt.setInt(8, currentPerson.getId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Person Updated."); //debug
            } else { //debug purpose, shouldnt happen
                System.out.println("Person not found.");
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //delete a person in the list and in the db
    public static void deletePerson(Person currentPerson) {
        if (currentPerson == null) {
            System.out.println("currentPerson null"); //debug
            return;
        }
        //personsList delete
        ObservableList<Person> persons = getPersons();
        persons.remove(currentPerson);

        // db update
        try {
            String query = "DELETE FROM person WHERE idperson=?";
            PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(query);
            stmt.setInt(1, currentPerson.getId());

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Person deleted."); //debug
            } else { //debug purpose, shouldnt happen
                System.out.println("Person not found.");
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static class PersonServiceHolder {
        private static final PersonService INSTANCE = new PersonService();
    }
}
