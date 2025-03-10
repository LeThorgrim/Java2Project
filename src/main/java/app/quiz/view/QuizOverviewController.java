/**
 * 
 */
package app.quiz.view;

import java.util.List;

import app.quiz.App;
import app.quiz.model.Question;
import app.quiz.service.QuestionService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

/**
 * @author Philippe Duval
 *
 *         This class holds all the logic of the display, this is where we mat
 *         our model (questions, answers, and so on) to our view (the beautiful
 *         fxml that you made with SceneBuilder)
 *
 */
public class QuizOverviewController {

	@FXML
	public Text questionText;

	@FXML
	public Button answerButton1;

	@FXML
	public Button answerButton2;

	@FXML
	public Button answerButton3;

	@FXML
	public Text scoreText;

	@FXML
	public Text totalQuestions;

	@FXML
	public Text currentQuestion;

	private Integer score = 0;

	private List<Question> questions;

	private int currentQuestionIndex;

	/**
	 * This method handles the mapping between our model and the different labels of
	 * the view
	 */
	private void showCurrentQuestion() {
		Question question = questions.get(currentQuestionIndex);
		questionText.setText(question.getQuestion());
		answerButton1.setText(question.getAnswers().get(0).getText());
		answerButton2.setText(question.getAnswers().get(1).getText());
		answerButton3.setText(question.getAnswers().get(2).getText());
		this.updateCurrentQuestionNumber();
	}

	/**
	 * This method handles the logic of the quiz : it advances to the next question
	 * of there are still questions to be answered, or it ends the journey if we are
	 * done.
	 */
	private void gotoNextQuestion() {
		if (this.currentQuestionIndex < this.questions.size() - 1) {
			this.currentQuestionIndex += 1;
			this.showCurrentQuestion();
		} else {
			this.gameOver();
		}
	}

	private void gameOver() {
		questionText.setText("");
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Game over");
		alert.setHeaderText("You achieved the marvellous score of " + this.score);
		// Go back to our main page, with the preferred method (see official documentation
		// here:
		// https://openjfx.io/javadoc/15/javafx.controls/javafx/scene/control/Alert.html
		alert.showAndWait();//.filter(response -> response == ButtonType.OK).ifPresent(response -> App.showView("HomeScreen"));
		App.showView("HomeScreen");
	}

	/**
	 * tests if the answerIndex corresponds to the good answer of the current
	 * question. If it is the case, it will increment the score. After this test,
	 * the method redirects the user to the next question.
	 * 
	 * @param answerIndex
	 */
	private void handleAnswer(int answerIndex) {
		if (this.questions.get(currentQuestionIndex).getAnswers().get(answerIndex).isGoodAnswer()) {
			this.incrementScore();
		}
		this.gotoNextQuestion();
	}

	/**
	 * Increments and prints the score
	 */
	private void incrementScore() {
		this.score += 1;
		this.printScore();
	}

	/**
	 * Just updates the scoreText field with the correct label
	 */
	private void printScore() {
		this.scoreText.setText("Your score is " + this.score);
	}

	@FXML
	private void handleAnswer1() {
		this.handleAnswer(0);
	}

	@FXML
	private void handleAnswer2() {
		this.handleAnswer(1);
	}

	@FXML
	private void handleAnswer3() {
		this.handleAnswer(2);
	}

	@FXML
	private void initialize() {
		// Get the questions from the QuestionService and store them in the questions
		// attribute;
		this.questions = QuestionService.getQuestions();
		// Initialize the currentQuestionIndex to 0;
		this.currentQuestionIndex = 0;
		this.totalQuestions.setText(Integer.toString(this.questions.size()));
		// Call the showCurrentQuestion() method.
		this.showCurrentQuestion();
		// Initialize the score text
		this.printScore();
	}

	/**
	 * This method handles the ugliness of the index conversion and string
	 * concatenation, in order to display a human readable score. using such a
	 * method helps keeping the code clean and explicit
	 */
	private void updateCurrentQuestionNumber() {
		this.currentQuestion.setText(Integer.toString(currentQuestionIndex + 1));
	}
}
