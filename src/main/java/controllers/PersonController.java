package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import db.daos.PersonDao;
import db.entities.Person;
import java.util.List;
import java.util.ArrayList;


public class PersonController {
    
    @FXML private TextField lastnameField;
    @FXML private TextField firstnameField;
    @FXML private TextField nicknameField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private TextField emailField;
    @FXML private TextField birthDateField;
    @FXML private Label messageLabel;
    @FXML private TextField deleteNicknameField;
    @FXML private TextField updateNicknameField;
    @FXML private TextField updateLastnameField;
    @FXML private TextField updateFirstnameField;
    @FXML private TextField updatePhoneField;
    @FXML private TextField updateAddressField;
    @FXML private TextField updateEmailField;
    @FXML private TextField updateBirthDateField; 
  

    private final PersonDao personDao = new PersonDao();

    @FXML
    private void handleAddPerson() {
        String lastname = lastnameField.getText().trim();
        String firstname = firstnameField.getText().trim();
        String nickname = nicknameField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();
        String email = emailField.getText().trim();
        String birthDate = birthDateField.getText().trim();

        if (lastname.isEmpty() || firstname.isEmpty()) {
            messageLabel.setText("Nom et prénom sont obligatoires !");
            return;
        }

        // Création de l'objet Person
        Person newPerson = new Person(lastname, firstname, nickname, phone, address, email, birthDate);
        
        // Ajout dans la base
        personDao.addPerson(newPerson);

        messageLabel.setText("Personne ajoutée avec succès !");
    }

    @FXML
    private void handleShowPeople() {
        List<Person> people = personDao.listPersons(); 
        if (people.isEmpty()) {
            messageLabel.setText("Aucune personne enregistrée.");
        } else {
            StringBuilder sb = new StringBuilder("Liste des personnes :\n");
            for (Person p : people) {
                sb.append(p.getFirstName()).append(" ").append(p.getLastName()).append(" ").append(p.getNickname()).append("\n");
            }
            messageLabel.setText(sb.toString());
        }
    }



    
    @FXML
    private void handleDeletePerson() {
        String nickname = deleteNicknameField.getText().trim();
        

        if (nickname.isEmpty()) {
            messageLabel.setText("Surnom obligatoire pour supprimer !");
            return;
        }else {
        	personDao.deletePerson(nickname);
        	messageLabel.setText("Personne avec ce surnom supprimé avec succès!");
        }

        
    }

    @FXML
    private void handleCancel() {
        lastnameField.clear();
        firstnameField.clear();
        nicknameField.clear();
        phoneField.clear();
        addressField.clear();
        emailField.clear();
        birthDateField.clear();
        messageLabel.setText("");
    }

    @FXML
    private void handleCancel2() {
        deleteNicknameField.clear();
    }
    
    @FXML
    private void handleSearchPerson() {
        String nickname = updateNicknameField.getText().trim();

        if (nickname.isEmpty()) {
            messageLabel.setText("Veuillez entrer un surnom pour la recherche !");
            return;
        }

        // Chercher la personne dans la base de données
        Person person = personDao.getPersonByNickname(nickname);

        if (person == null) {
            messageLabel.setText("Aucune personne trouvée avec ce surnom.");
            return;
        }

        // Remplir les champs avec les données trouvées
        updateLastnameField.setText(person.getLastName());
        updateFirstnameField.setText(person.getFirstName());
        updatePhoneField.setText(person.getPhoneNumber());
        updateAddressField.setText(person.getAddress());
        updateEmailField.setText(person.getEmailAddress());
        updateBirthDateField.setText(person.getBirthDate());

        messageLabel.setText("Données chargées avec succès !");
    }
    
    
    @FXML
    private void handleUpdatePerson() {
        String nickname = updateNicknameField.getText().trim();
        String lastname = updateLastnameField.getText().trim();
        String firstname = updateFirstnameField.getText().trim();
        String phone = updatePhoneField.getText().trim();
        String address = updateAddressField.getText().trim();
        String email = updateEmailField.getText().trim();
        String birthDate = updateBirthDateField.getText().trim();

        if (nickname.isEmpty()) {
            messageLabel.setText("Le surnom est obligatoire pour identifier la personne !");
            return;
        }

        // Vérifier si la personne existe avant la mise à jour
        Person existingPerson = personDao.getPersonByNickname(nickname);
        if (existingPerson == null) {
            messageLabel.setText("Aucune personne trouvée avec ce surnom !");
            return;
        }

        // Si un champ est vide, conserver l'ancienne valeur
        if (lastname.isEmpty()) lastname = existingPerson.getLastName();
        if (firstname.isEmpty()) firstname = existingPerson.getFirstName();
        if (phone.isEmpty()) phone = existingPerson.getPhoneNumber();
        if (address.isEmpty()) address = existingPerson.getAddress();
        if (email.isEmpty()) email = existingPerson.getEmailAddress();
        if (birthDate.isEmpty()) birthDate = existingPerson.getBirthDate();

        personDao.updatePerson(nickname, lastname, firstname, phone, address, email, birthDate);
        messageLabel.setText("Personne mise à jour avec succès !");
        
    }
    @FXML
    private void handleCancel3() {
        updateNicknameField.clear();
        updateLastnameField.clear();
        updateFirstnameField.clear();
        updatePhoneField.clear();
        updateAddressField.clear();
        updateEmailField.clear();
        updateBirthDateField.clear();
        messageLabel.setText("");
    }

}
