package app.service;

import app.db.daos.DatabaseManager;
import app.db.daos.PersonDao;
import app.db.entities.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        if (person == null) {//shouldnt happen but just in case
            System.out.println("currentperson null");
            return;
        }
        //add to db
        try {
            String query = "INSERT INTO person (lastName, firstName, nickname, phone_number, address, email_address, birth_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, person.getLastName());
            stmt.setString(2, person.getFirstName());
            stmt.setString(3, person.getNickname());
            stmt.setString(4, person.getPhoneNumber());
            stmt.setString(5, person.getAddress());
            stmt.setString(6, person.getEmailAddress());
            stmt.setString(7, person.getBirthDate());

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                // Get the generated ID
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    person.setId(rs.getInt(1)); // Set the new ID
                }
                rs.close();
            } else {
                System.out.println("Error in addPerson db");
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        // add to ObservableList
        ObservableList<Person> persons = getPersons();
        persons.add(person);
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
