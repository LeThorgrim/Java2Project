package daos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import db.daos.*;

import db.entities.Person;

public class PersonDaoTestCase {

    private PersonDao personDao = new PersonDao();

    @BeforeEach
    public void initDatabase() throws Exception {
        Connection connection = DatabaseManager.getConnection();
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS person ("
                + "lastname VARCHAR(50) NOT NULL,"
                + "firstname VARCHAR(50) NOT NULL,"
                + "nickname VARCHAR(50) PRIMARY KEY,"
                + "phone_number VARCHAR(15),"
                + "address VARCHAR(255),"
                + "email_address VARCHAR(100),"
                + "birth_date VARCHAR(10)"
                + ");");
        stmt.executeUpdate("DELETE FROM person");
        stmt.executeUpdate("INSERT INTO person(lastname, firstname, nickname, phone_number, address, email_address, birth_date) "
                + "VALUES ('Doe', 'John', 'jdoe', '1234567890', '123 Street', 'johndoe@example.com', '1990-01-01')");
        stmt.executeUpdate("INSERT INTO person(lastname, firstname, nickname, phone_number, address, email_address, birth_date) "
                + "VALUES ('Smith', 'Jane', 'jsmith', '0987654321', '456 Avenue', 'janesmith@example.com', '1992-02-02')");
        stmt.close();
        connection.close();
    }

    @Test
    public void shouldListPersons() {
        // WHEN
        List<Person> persons = personDao.listPersons();
        // THEN
        assertThat(persons).hasSize(2);
        assertThat(persons).extracting("nickname", "lastName", "firstName").containsOnly(
                tuple("jdoe", "Doe", "John"),
                tuple("jsmith", "Smith", "Jane")
        );
    }

    @Test
    public void shouldGetPersonByNickname() {
        // WHEN
        Person person = personDao.getPersonByNickname("jdoe");
        // THEN
        assertThat(person).isNotNull();
        assertThat(person.getLastName()).isEqualTo("Doe");
        assertThat(person.getFirstName()).isEqualTo("John");
    }

    @Test
    public void shouldNotGetUnknownPerson() {
        // WHEN
        Person person = personDao.getPersonByNickname("unknown");
        // THEN
        assertThat(person).isNull();
    }

    @Test
    public void shouldAddPerson() throws Exception {
        // GIVEN
        Person newPerson = new Person("Brown", "Charlie", "cbrown", "1112223333", "789 Road", "cbrown@example.com", "1995-03-03");
        // WHEN 
        personDao.addPerson(newPerson);
        // THEN
        Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM person WHERE nickname='cbrown'");
        assertThat(resultSet.next()).isTrue();
        assertThat(resultSet.getString("lastname")).isEqualTo("Brown");
        assertThat(resultSet.getString("firstname")).isEqualTo("Charlie");
        resultSet.close();
        statement.close();
        connection.close();
    }

    @Test
    public void shouldDeletePerson() throws Exception {
        // WHEN
        personDao.deletePerson("jdoe");
        // THEN
        Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM person WHERE nickname='jdoe'");
        assertThat(resultSet.next()).isFalse();
        resultSet.close();
        statement.close();
        connection.close();
    }

    @Test
    public void shouldUpdatePerson() throws Exception {
        // WHEN
        personDao.updatePerson("jdoe", "DoeUpdated", "JohnUpdated", "5556667777", "New Street", "newemail@example.com", "2000-01-01");
        // THEN
        Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM person WHERE nickname='jdoe'");
        assertThat(resultSet.next()).isTrue();
        assertThat(resultSet.getString("lastname")).isEqualTo("DoeUpdated");
        assertThat(resultSet.getString("firstname")).isEqualTo("JohnUpdated");
        resultSet.close();
        statement.close();
        connection.close();
    }
}
