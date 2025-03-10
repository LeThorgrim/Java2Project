package app.quiz.util;

import app.quiz.model.Question;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class QuestionValueFactory
		implements Callback<TableColumn.CellDataFeatures<Question, String>, ObservableValue<String>> {

	@Override
	public ObservableValue<String> call(CellDataFeatures<Question, String> cellData) {
		return new SimpleStringProperty(cellData.getValue().getQuestion());
	}
}
