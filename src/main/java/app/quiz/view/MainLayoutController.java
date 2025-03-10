package app.quiz.view;

import app.quiz.App;
import javafx.application.Platform;

public class MainLayoutController {

	public void closeApplication() {
		Platform.exit();
	}

	public void gotoHome() {
		Platform.runLater(() -> {
			App.showView("HomeScreen");
		});
	}

	public void gotoQuestionAdmin() {
		Platform.runLater(() -> {
			App.showView("QuestionsAdmin");
		});
	}

}
