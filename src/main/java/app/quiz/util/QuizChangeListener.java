package app.quiz.util;

import app.quiz.model.Question;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public abstract class QuizChangeListener implements ChangeListener<Question> {

	@Override
	public void changed(ObservableValue<? extends Question> observable, Question oldValue, Question newValue) {
		handleNewValue(newValue);

	}

	public abstract void handleNewValue(Question newValue);

}
