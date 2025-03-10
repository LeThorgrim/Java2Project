package daos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import app.db.daos.DataSourceFactory;
import app.db.daos.DatabaseManager;
import app.db.daos.PersonDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import app.db.entities.Person;

public class PersonDaoTestCase {

    private PersonDao personDao = new PersonDao();

    @BeforeEach
    public void initDatabase() throws Exception {
        // init db w/ DatabaseManager
        DatabaseManager.initializeDatabase();

        // test data
        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM person"); // clear table
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='person'"); // AUTO_INCREMENT reset

            // 3 test data
            stmt.executeUpdate("INSERT INTO person(lastname, firstname, nickname, phone_number, address, email_address, birth_date) " +
                    "VALUES ('Doe', 'John', 'Johnny', '123456789', '123 Street', 'john.doe@email.com', '1990-01-01')");
            stmt.executeUpdate("INSERT INTO person(lastname, firstname, nickname, phone_number, address, email_address, birth_date) " +
                    "VALUES ('Smith', 'Alice', 'Ali', '987654321', '456 Avenue', 'alice.smith@email.com', '1995-05-05')");
            stmt.executeUpdate("INSERT INTO person(lastname, firstname, nickname, phone_number, address, email_address, birth_date) " +
                    "VALUES ('Brown', 'Bob', 'Bobby', '555444333', '789 Boulevard', 'bob.brown@email.com', '1988-12-12')");
        }
    }

    @AfterEach
    void resetDatabase() { // clear table after each test
        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM person;");  // delete every row
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='person';"); // re-init auto-incr
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldListPersons() {
        // WHEN
        List<Person> persons = personDao.listPersons();
        // THEN
        assertThat(persons).hasSize(3);
        assertThat(persons).extracting("lastName", "firstName", "nickname")
                .containsOnly(
                        tuple("Doe", "John", "Johnny"),
                        tuple("Smith", "Alice", "Ali"),
                        tuple("Brown", "Bob", "Bobby")
                );
    }


    @Test
    public void shouldAddPerson() throws Exception {
        // WHEN
        Person newPerson = new Person("Taylor", "Emma", "Em", "333222111",
                "999 Road", "emma.taylor@email.com", "2000-07-07");
        personDao.addPerson(newPerson);

        // THEN
        assertThat(newPerson.getId()).isNotNull(); // Vérifie que l'ID a bien été récupéré
        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery("SELECT * FROM person WHERE idperson = " + newPerson.getId())) {
            assertThat(resultSet.next()).isTrue();
            assertThat(resultSet.getString("lastname")).isEqualTo("Taylor");
        }
    }

    @Test
    public void shouldUpdatePerson() throws Exception {
        // GIVEN
        Person person = new Person("White", "Walter", "Heisenberg", "666777888",
                "Blue Meth St.", "walter.white@email.com", "1960-09-07");
        personDao.addPerson(person);

        // WHEN
        personDao.updatePerson(person.getId(), "Heisenberg", "White", "Walter", "999888777",
                "New Address", "new.email@email.com", "1960-09-07");

        // THEN
        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery("SELECT * FROM person WHERE idperson = " + person.getId())) {
            assertThat(resultSet.next()).isTrue();
            assertThat(resultSet.getString("phone_number")).isEqualTo("999888777");
            assertThat(resultSet.getString("address")).isEqualTo("New Address");
        }
    }

    @Test
    public void shouldDeletePerson() throws Exception {
        // GIVEN
        Person person = new Person("White", "Walter", "Heisenberg", "666777888",
                "Blue Meth St.", "walter.white@email.com", "1960-09-07");
        personDao.addPerson(person);

        // WHEN
        personDao.deletePerson(person.getId());

        // THEN
        try (Connection connection = DataSourceFactory.getDataSource().getConnection();
             Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery("SELECT * FROM person WHERE idperson = " + person.getId())) {
            assertThat(resultSet.next()).isFalse(); // La personne ne doit plus exister
        }
    }
}
