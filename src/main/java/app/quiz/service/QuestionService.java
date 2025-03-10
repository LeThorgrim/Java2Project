package app.quiz.service;

import app.quiz.model.Answer;
import app.quiz.model.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class QuestionService {

	private ObservableList<Question> questions;

	private QuestionService() {
		questions = FXCollections.observableArrayList();
		questions.add(new Question("Who is the Tony Stark's personnal assistant?", new Answer("Jarvis", false),
				new Answer("Pepper Pots", true), new Answer("Alfred", false)));
		questions.add(new Question("He is red & blue and he is a member of the Justice League.",
				new Answer("Spiderman", false), new Answer("Captain America", false), new Answer("Superman", true)));
		questions.add(new Question("What is the type of ray that irradiated Bruce Banner?",
				new Answer("Gamma ray", true), new Answer("X ray", false), new Answer("Blue ray", false)));
		questions.add(new Question("What is the other name of Bucky Barnes?", new Answer("The Joker", false),
				new Answer("Two-Face", false), new Answer("The Winter Soldier", true)));
		questions.add(new Question("Billionaire, Playboy and Engineer.", new Answer("Tony Stark", true),
				new Answer("Your Java2 teacher", false), new Answer("Bruce Wayne", false)));
		questions.add(new Question("He appears in a cameo in each Marvel's movie.", new Answer("Donald Trump", false),
				new Answer("Bruce Lee", false), new Answer("Stan Lee", true)));
		questions.add(new Question("Who is Steve Rogers?", new Answer("Captain America", true),
				new Answer("Superman", false), new Answer("Batman", false)));
		questions.add(new Question("If you mix Jarvis, an Infinity gem and a robot, what do you get?",
				new Answer("The Vision", true), new Answer("Iron man", false), new Answer("Extremis", false)));
		questions.add(new Question("Where is the asylum where Batman's ennemies are sent?", new Answer("Gotham", false),
				new Answer("NYC", false), new Answer("Arkham", true)));
		questions.add(new Question("This super-hero is pretty fast...", new Answer("HTML5", false),
				new Answer("Flash", true), new Answer("Unity", false)));

	}

	public static ObservableList<Question> getQuestions() {
		return QuestionServiceHolder.INSTANCE.questions;
	}

	public static void addQuestion(Question question) {
		QuestionServiceHolder.INSTANCE.questions.add(question);
	}

	private static class QuestionServiceHolder {
		private static final QuestionService INSTANCE = new QuestionService();
	}

}
