/**
 * 
 */
package app.quiz.view;

import java.io.IOException;

import app.quiz.App;
import javafx.fxml.FXML;

/**
 * @author Philippe Duval
 * 
 * This is the controller of the home screen. It is very simple, and just handles the change to the quiz view
 *
 */
public class HomeScreenController {

	@FXML
	public void handleLaunchButton() throws IOException {
		// Here we make use of our new method allowing us to change views inside the main Parent
		App.showView("QuizOverview");
	}
}
