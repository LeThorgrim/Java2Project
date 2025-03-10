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

    // show the details of a person in the form when you click on it
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

    //updates a person that you modified
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
        } else { //meaning we are creating a new person
            currentPerson = new Person(lastNameField.getText(), firstNameField.getText(),
                    nicknameField.getText(), phoneField.getText(), addressField.getText(),
                    emailField.getText(),birthField.getText());
            //PersonService handle the addition
            PersonService.addPerson(currentPerson);
            System.out.println(currentPerson.getId() + " added."); //debug

            refreshList();
            formPane.setVisible(false); //hide the form as we deleted the person
            currentPerson = null; //update
        }
    }

    //delete a person
    public void handleDeleteButton(ActionEvent actionEvent) {
        if (currentPerson != null) { //if a person is selected
            // alert for the deletion
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + currentPerson.getId() + " ?",
                    ButtonType.YES, ButtonType.NO); //yes/no confirmation box
            alert.setTitle("Deletion confirmation"); // title
            alert.setHeaderText(null); // no header

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) { //yes
                    // deletion using PersonService
                    PersonService.deletePerson(currentPerson);

                    // Mise à jour de la liste
                    refreshList();
                    formPane.setVisible(false); //hide the form as we deleted the person
                    currentPerson = null; //update
                }
            });
        } else {
            System.out.println("No person selected."); //debug
        }
    }

    //show the form to add a new person
    // Show the form to add a new person
    public void handleNewButton(ActionEvent actionEvent) {
        // Show the form in case we have no person selected
        formPane.setVisible(true);
        // Clear all input fields in case we were editing a person
        lastNameField.clear();
        firstNameField.clear();
        nicknameField.clear();
        phoneField.clear();
        addressField.clear();
        emailField.clear();
        birthField.clear();

        // Reset currentPerson (important to differentiate new entry from edit)
        currentPerson = null;

        System.out.println("Forms opened.");//debug
    }

}
