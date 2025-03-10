package app.quiz.view;

import app.quiz.App;
import javafx.application.Platform;

public class MainLayoutController {

	public void closeApplication() {
		Platform.exit();
	}

	/* Some things that I no longer need with the remove of navbar
	public void gotoHome() {
		Platform.runLater(() -> {
			App.showView("HomeScreen");
		});
	}
	*/

	/*
	public void gotoQuestionAdmin() {
		Platform.runLater(() -> {
			App.showView("QuestionsAdmin");
		});
	}
	*/

}
