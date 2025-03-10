package db.daos;

import db.entities.Person;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.sql.PreparedStatement;

public class PersonDao {

    public List<Person> listPersons() {
        List<Person> listOfPersons = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet results = statement.executeQuery("SELECT * FROM person")) {
                    while (results.next()) {
                        Person person = new Person(
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
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO person (lastname, firstname, nickname, phone_number, address, email_address, birth_date) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                statement.setString(1, person.getLastName());
                statement.setString(2, person.getFirstName());
                statement.setString(3, person.getNickname());
                statement.setString(4, person.getPhoneNumber());
                statement.setString(5, person.getAddress());
                statement.setString(6, person.getEmailAddress());
                statement.setString(7, person.getBirthDate());

                statement.executeUpdate();
                System.out.println("Nouvelle personne ajoutée avec succès.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePerson(String nickname) {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement statement =
                         connection.prepareStatement("DELETE FROM person WHERE nickname = ?")) {
                statement.setString(1, nickname);
                statement.executeUpdate();
                System.out.println("Personne avec le surnom '" + nickname + "' supprimée avec succès.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updatePerson(String nickname, String lastname, String firstname,
            String phoneNumber, String address, String email, String birthDate) {
    			try (Connection connection = DatabaseManager.getConnection()) {
// Vérifier si la personne existe
    				Person existingPerson = getPersonByNickname(nickname);
    				if (existingPerson == null) {
    					System.out.println("Aucune personne trouvée avec le surnom '" + nickname + "'.");
    					return;
    					}
// Construire la requête SQL dynamique
    				String sql = "UPDATE person SET ";
					List<String> fields = new ArrayList<>();
					List<String> values = new ArrayList<>();

					if (!lastname.isEmpty()) {
						fields.add("lastname = ?");
						values.add(lastname);
						}
					if (!firstname.isEmpty()) {
					fields.add("firstname = ?");
					values.add(firstname);
					}
					if (!phoneNumber.isEmpty()) {
					fields.add("phone_number = ?");
					values.add(phoneNumber);
					}
					if (!address.isEmpty()) {
					fields.add("address = ?");
					values.add(address);
					}
					if (!email.isEmpty()) {
					fields.add("email_address = ?");
					values.add(email);
					}
					if (!birthDate.isEmpty()) {
					fields.add("birth_date = ?");
					values.add(birthDate);
					}

					if (fields.isEmpty()) {
					System.out.println("Aucune mise à jour effectuée.");
					return;
					}

					sql += String.join(", ", fields) + " WHERE nickname = ?";
					try (PreparedStatement statement = connection.prepareStatement(sql)) {
						for (int i = 0; i < values.size(); i++) {
							statement.setString(i + 1, values.get(i));
							}
						statement.setString(values.size() + 1, nickname);
						int rowsUpdated = statement.executeUpdate();
						if (rowsUpdated > 0) {
							System.out.println("Personne avec le surnom '" + nickname + "' mise à jour avec succès.");
							} else {
								System.out.println("Échec de la mise à jour.");
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
