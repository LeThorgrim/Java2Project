package daos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import db.daos.DataSourceFactory;
import db.daos.DatabaseManager;
import db.daos.PersonDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import db.entities.Person;

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
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='person'"); // AUTO_INCREMENT renit

            // 3 test datas
            stmt.executeUpdate("INSERT INTO person(lastname, firstname, nickname, phone_number, address, email_address, birth_date) " +
                    "VALUES ('Doe', 'John', 'Johnny', '123456789', '123 Street', 'john.doe@email.com', '1990-01-01')");
            stmt.executeUpdate("INSERT INTO person(lastname, firstname, nickname, phone_number, address, email_address, birth_date) " +
                    "VALUES ('Smith', 'Alice', 'Ali', '987654321', '456 Avenue', 'alice.smith@email.com', '1995-05-05')");
            stmt.executeUpdate("INSERT INTO person(lastname, firstname, nickname, phone_number, address, email_address, birth_date) " +
                    "VALUES ('Brown', 'Bob', 'Bobby', '555444333', '789 Boulevard', 'bob.brown@email.com', '1988-12-12')");
        }
    }

    @Test
    public void shouldListPersons() {
        // WHEN
        List<Person> persons = personDao.listPersons();
        // THEN
        assertThat(persons).hasSize(3);
        assertThat(persons).extracting("lastname", "firstname", "nickname")
                .containsOnly(
                        tuple("Doe", "John", "Johnny"),
                        tuple("Smith", "Alice", "Ali"),
                        tuple("Brown", "Bob", "Bobby")
                );
    }

    @Test
    public void shouldGetPersonByNickname() {
        // WHEN
        Person person = personDao.getPersonByNickname("Ali");
        // THEN
        assertThat(person.getLastName()).isEqualTo("Smith");
        assertThat(person.getFirstName()).isEqualTo("Alice");
        assertThat(person.getEmailAddress()).isEqualTo("alice.smith@email.com");
    }

    @Test
    public void shouldNotGetUnknownPerson() {
        // WHEN
        Person person = personDao.getPersonByNickname("Unknown");
        // THEN
        assertThat(person).isNull();
    }

    @Test
    public void shouldAddPerson() throws Exception {
        // WHEN
        personDao.addPerson(new Person("Taylor", "Emma", "Em", "333222111",
                "999 Road", "emma.taylor@email.com", "2000-07-07"));
        // THEN
        Connection connection = DataSourceFactory.getDataSource().getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM person WHERE nickname='Em'");

        assertThat(resultSet.next()).isTrue();
        assertThat(resultSet.getInt("idperson")).isNotNull();
        assertThat(resultSet.getString("lastname")).isEqualTo("Taylor");
        assertThat(resultSet.getString("firstname")).isEqualTo("Emma");
        assertThat(resultSet.getString("nickname")).isEqualTo("Em");
        assertThat(resultSet.getString("email_address")).isEqualTo("emma.taylor@email.com");
        assertThat(resultSet.next()).isFalse();

        resultSet.close();
        statement.close();
        connection.close();
    }

    @Test
    public void shouldUpdatePerson() throws Exception {
        // GIVEN
        personDao.addPerson(new Person("White", "Walter", "Heisenberg", "666777888",
                "Blue Meth St.", "walter.white@email.com", "1960-09-07"));

        // WHEN
        personDao.updatePerson("Heisenberg", "White", "Walter", "999888777",
                "New Address", "new.email@email.com", "1960-09-07");

        // THEN
        Person updatedPerson = personDao.getPersonByNickname("Heisenberg");
        assertThat(updatedPerson).isNotNull();
        assertThat(updatedPerson.getPhoneNumber()).isEqualTo("999888777");
        assertThat(updatedPerson.getAddress()).isEqualTo("New Address");
        assertThat(updatedPerson.getEmailAddress()).isEqualTo("new.email@email.com");
    }

    @Test
    public void shouldDeletePerson() throws Exception {
        // GIVEN
        personDao.addPerson(new Person("White", "Walter", "Heisenberg", "666777888",
                "Blue Meth St.", "walter.white@email.com", "1960-09-07"));

        // WHEN
        personDao.deletePerson("Heisenberg");

        // THEN
        Person deletedPerson = personDao.getPersonByNickname("Heisenberg");
        assertThat(deletedPerson).isNull();
    }
}