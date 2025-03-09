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
}
