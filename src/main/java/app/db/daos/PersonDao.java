package app.db.daos;

import app.db.entities.Person;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.sql.PreparedStatement;

public class PersonDao {

    //retrieve every person from the db
    public List<Person> listPersons() {
        List<Person> listOfPersons = new ArrayList<>();
        //connection using our "wrapper" of datasourcefactory
        try (Connection connection = DatabaseManager.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet results = statement.executeQuery("SELECT * FROM person")) { //sql query
                    while (results.next()) { //results
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

    public void addPerson(Person person) {
        //connection using our "wrapper" of datasourcefactory
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO person (lastname, firstname, nickname, phone_number, address, email_address, birth_date) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)")) { //query
                statement.setString(1, person.getLastName());
                statement.setString(2, person.getFirstName());
                statement.setString(3, person.getNickname());
                statement.setString(4, person.getPhoneNumber());
                statement.setString(5, person.getAddress());
                statement.setString(6, person.getEmailAddress());
                statement.setString(7, person.getBirthDate());

                statement.executeUpdate();
                System.out.println("New person added."); //debug
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePerson(int id) {
        //connection using our "wrapper" of datasourcefactory
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement statement =
                         connection.prepareStatement("DELETE FROM person WHERE idperson = ?")) {
                statement.setInt(1, id);
                statement.executeUpdate();
                System.out.println("person id: " + id + " deleted."); //debug
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //need some changes (using id) (comments & translate)
    public void updatePerson(String nickname, String lastname, String firstname,
                             String phoneNumber, String address, String email, String birthDate) {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE person SET lastname = ?, firstname = ?, phone_number = ?, " +
                            "address = ?, email_address = ?, birth_date = ? WHERE nickname = ?")) {
                statement.setString(1, lastname);
                statement.setString(2, firstname);
                statement.setString(3, phoneNumber);
                statement.setString(4, address);
                statement.setString(5, email);
                statement.setString(6, birthDate);
                statement.setString(7, nickname);

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Personne avec le surnom '" + nickname + "' mise à jour avec succès.");
                } else {
                    System.out.println("Aucune personne trouvée avec le surnom '" + nickname + "'.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Person getPersonByNickname(String nickname) {
        Person person = null;

        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM person WHERE nickname = ?")) {
                statement.setString(1, nickname);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        person = new Person(
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
}
