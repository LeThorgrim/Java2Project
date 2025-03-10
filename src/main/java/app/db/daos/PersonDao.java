package app.db.daos;

import app.db.entities.Person;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.sql.PreparedStatement;

// DAO only interacts with the database
public class PersonDao {

    //retrieve all data
    public List<Person> listPersons() {
        List<Person> listOfPersons = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection()) { //connection
            try (Statement statement = connection.createStatement()) {
                try (ResultSet results = statement.executeQuery("SELECT * FROM person")) { // query
                    while (results.next()) {
                        Person person = new Person(
                                results.getInt("idperson"),
                                results.getString("lastname"),
                                results.getString("firstname"),
                                results.getString("nickname"),
                                results.getString("phone_number"),
                                results.getString("address"),
                                results.getString("email_address"),
                                results.getString("birth_date")
                        );
                        listOfPersons.add(person);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfPersons;
    }

    //add new data
    public void addPerson(Person person) {
        try (Connection connection = DatabaseManager.getConnection()) { //connection
            // query
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO person (lastname, firstname, nickname, phone_number, address, email_address, birth_date) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

                statement.setString(1, person.getLastName());
                statement.setString(2, person.getFirstName());
                statement.setString(3, person.getNickname());
                statement.setString(4, person.getPhoneNumber());
                statement.setString(5, person.getAddress());
                statement.setString(6, person.getEmailAddress());
                statement.setString(7, person.getBirthDate());

                int rowsInserted = statement.executeUpdate();

                // gets the generated db ID
                if (rowsInserted > 0) {
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            person.setId(generatedKeys.getInt(1));
                        }
                    }
                }
                System.out.println("New person added with ID: " + person.getId()); // Debug
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //delete data
    public void deletePerson(int id) {
        try (Connection connection = DatabaseManager.getConnection()) { //connection
            // query
            try (PreparedStatement statement =
                         connection.prepareStatement("DELETE FROM person WHERE idperson = ?")) {
                statement.setInt(1, id); //arg '?' 1 = id
                statement.executeUpdate(); //execute
                System.out.println("Person with ID " + id + " deleted.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //update data
    public void updatePerson(int id, String nickname, String lastname, String firstname,
                             String phoneNumber, String address, String email, String birthDate) {
        try (Connection connection = DatabaseManager.getConnection()) { //connection
            // query
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE person SET lastname = ?, firstname = ?, nickname = ?, phone_number = ?, " +
                            "address = ?, email_address = ?, birth_date = ? WHERE idperson = ?")) {
                statement.setString(1, lastname); //arg '?' 1 = id
                statement.setString(2, firstname); //arg '?' 2 = id
                statement.setString(3, nickname); //...
                statement.setString(4, phoneNumber);
                statement.setString(5, address);
                statement.setString(6, email);
                statement.setString(7, birthDate);
                statement.setInt(8, id);

                int rowsUpdated = statement.executeUpdate(); //execute
                if (rowsUpdated > 0) { //if updated
                    System.out.println("Person with ID " + id + " updated successfully.");
                } else { //if not updated (should not happen)
                    System.out.println("No person found with ID " + id + ".");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //get data by nickname, which 'in retrospection' is not useful (that's why its commented)
    /*
    public Person getPersonByNickname(String nickname) {
        Person person = null;
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM person WHERE nickname = ?")) {
                statement.setString(1, nickname);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        person = new Person(
                                resultSet.getInt("idperson"),
                                resultSet.getString("lastname"),
                                resultSet.getString("firstname"),
                                resultSet.getString("nickname"),
                                resultSet.getString("phone_number"),
                                resultSet.getString("address"),
                                resultSet.getString("email_address"),
                                resultSet.getString("birth_date")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }
    */
}