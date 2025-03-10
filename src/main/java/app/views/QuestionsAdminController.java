package app.views;

import app.db.entities.Person;
import app.utils.PersonValueFactory;
import app.service.PersonService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.collections.ObservableList;

public class QuestionsAdminController {

    @FXML
    private TableView<Person> personTable;

    @FXML
    private TableColumn<Person, String> personColumn;

    @FXML
    private Pane formPane;

    //fields
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField nicknameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField birthField;

    //buttons
    @FXML
    private RadioButton radio1;
    @FXML
    private RadioButton radio2;
    @FXML
    private RadioButton radio3;

    private Person currentPerson;

    @FXML
    public void initialize() {
        personColumn.setCellValueFactory(new PersonValueFactory());
        populateList();

        // selection listener for TableView
        personTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showQuestionDetails(newValue)
        );
    }

    public void refreshList() {
        personTable.refresh();
        personTable.getSelectionModel().clearSelection();
    }

    public void populateList() { //get the list of persons from the service + refresh
        ObservableList<Person> persons = PersonService.getPersons();
        personTable.setItems(persons);
        System.out.println("Persons retrieved: " + persons.size()); // Debug
        refreshList();
    }

    public void showQuestionDetails(Person personToShow){
        if (personToShow != null) {
            formPane.setVisible(true);
            currentPerson = personToShow;
            lastNameField.setText(currentPerson.getLastName());
            firstNameField.setText(currentPerson.getFirstName());
            nicknameField.setText(currentPerson.getNickname());
            phoneField.setText(currentPerson.getPhoneNumber());
            addressField.setText(currentPerson.getAddress());
            emailField.setText(currentPerson.getEmailAddress());
            birthField.setText(currentPerson.getBirthDate());
        } else {
            formPane.setVisible(false);
        }
    }

    public void handleSaveButton(ActionEvent actionEvent) {
        if (currentPerson != null) {
            // take the new (or not) infos from the fields
            currentPerson.setLastName(lastNameField.getText());
            currentPerson.setFirstName(firstNameField.getText());
            currentPerson.setNickname(nicknameField.getText());
            currentPerson.setPhoneNumber(phoneField.getText());
            currentPerson.setAddress(addressField.getText());
            currentPerson.setEmailAddress(emailField.getText());
            currentPerson.setBirthDate(birthField.getText());
            // save and send to db
            PersonService.updatePerson(currentPerson);
            // refresh
            refreshList();

            System.out.println(currentPerson.getId() + " updated."); //debug
        } else {
            System.out.println("Nobody selected."); //debug
        }
    }


    public void handleDeleteButton(ActionEvent actionEvent) {
    }

    public void handleNewButton(ActionEvent actionEvent) {
    }
}
