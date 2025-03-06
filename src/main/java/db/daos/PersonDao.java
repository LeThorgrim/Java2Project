package db.daos;

import db.entities.Person;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.sql.DataSource;
import java.sql.PreparedStatement;

public class PersonDao {

    private DataSource getDataSource() {
        return DataSourceFactory.getDataSource();
    }

    public List<Person> listPersons() {
        List<Person> listOfPersons = new ArrayList<>();

        // Connexion à la base de données
        try (Connection connection = getDataSource().getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet results = statement.executeQuery("SELECT * FROM person")) {
                    while (results.next()) {
                        // Création d'un objet Person pour chaque ligne de la table
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

    public void addPerson(String lastname, String firstname, String nickname,
                          String phoneNumber, String address, String email, String birthDate) {
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO person (lastname, firstname, nickname, phone_number, address, email_address, birth_date) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                statement.setString(1, lastname);
                statement.setString(2, firstname);
                statement.setString(3, nickname);
                statement.setString(4, phoneNumber);
                statement.setString(5, address);
                statement.setString(6, email);
                statement.setString(7, birthDate);

                statement.executeUpdate();
                System.out.println("Nouvelle personne ajoutée avec succès.");
            }
        }  catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePerson(Integer personId) {
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement statement =
                         connection.prepareStatement("DELETE FROM person WHERE idperson = ?")) {
                statement.setInt(1, personId);
                statement.executeUpdate();
                System.out.println("Personne avec l'ID " + personId + " supprimée avec succès.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updatePerson(Integer personId, String lastname, String firstname, String nickname,
                             String phoneNumber, String address, String email, String birthDate) {
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE person SET lastname = ?, firstname = ?, nickname = ?, phone_number = ?, " +
                            "address = ?, email_address = ?, birth_date = ? WHERE idperson = ?")) {
                statement.setString(1, lastname);
                statement.setString(2, firstname);
                statement.setString(3, nickname);
                statement.setString(4, phoneNumber);
                statement.setString(5, address);
                statement.setString(6, email);
                statement.setString(7, birthDate);
                statement.setInt(8, personId);


                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Personne avec l'ID " + personId + " mise à jour avec succès.");
                } else {
                    System.out.println("Aucune personne trouvée avec l'ID " + personId + ".");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {

        DatabaseManager.initializeDatabase();

        PersonDao dao = new PersonDao();
        // dao.updatePerson(2, "Durand", "Paul", "Polo", "0612345678",
        //     "45 Rue de Lyon", "paul.durand@email.com", "1988-07-23");
        //dao.addPerson("Martin", "Alice", "Ali", "0654321890",
        //"12 Rue des Lilas", "alice.martin@email.com", "1995-04-10");

        List<Person> persons = dao.listPersons();
        for (Person p : persons) {
            System.out.println(p);
        }
    }



}



