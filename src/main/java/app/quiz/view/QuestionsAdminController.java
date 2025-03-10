package app.quiz.view;

import app.db.entities.Person;
import app.quiz.util.QuestionValueFactory;
import app.service.PersonService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import app.quiz.model.Question;
import app.quiz.service.QuestionService;
import javafx.collections.ObservableList;

public class QuestionsAdminController {

    @FXML
    private TableView<Person> personTable;

    @FXML
    private TableColumn<Question, String> questionColumn;

    @FXML
    private Pane formPane;

    //fields
    @FXML
    private TextField LastNameField;
    @FXML
    private TextField FirstNameField;
    @FXML
    private TextField NicknameField;
    @FXML
    private TextField PhoneField;
    @FXML
    private TextField AddressField;
    @FXML
    private TextField EmailField;
    @FXML
    private TextField BirthField;

    @FXML
    private RadioButton radio1;

    @FXML
    private RadioButton radio2;

    @FXML
    private RadioButton radio3;

    private Question currentQuestion;

    @FXML
    public void initialize() {
        questionColumn.setCellValueFactory(new QuestionValueFactory());
        populateList();
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

    /*public void showQuestionDetails(Question questionToShow){
        if (questionToShow != null) {
            formPane.setVisible(true);
            currentQuestion = questionToShow;
            questionField.setText(currentQuestion.getQuestion());
            answer1Field.setText(currentQuestion.getAnswer1().getAnswer());
            answer2Field.setText(currentQuestion.getAnswer2().getAnswer());
            answer3Field.setText(currentQuestion.getAnswer3().getAnswer());
        } else {
            formPane.setVisible(false);
        }
    }*/

    public void handleSaveButton(ActionEvent actionEvent) {
    }

    public void handleDeleteButton(ActionEvent actionEvent) {
    }

    public void handleNewButton(ActionEvent actionEvent) {
    }
}
