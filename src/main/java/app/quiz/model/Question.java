package app.quiz.model;

import java.util.Arrays;
import java.util.List;

public class Question {

	private String question;

	private Answer answer1;
	private Answer answer2;
	private Answer answer3;

	private List<Answer> answers;

	public Question(String question, Answer answer1, Answer answer2, Answer answer3) {
		super();
		this.question = question;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.answers = Arrays.asList(answer1, answer2, answer3);
	}

	public Question() {
		this("Question", new Answer("answer 1", true), new Answer("answer 2", false), new Answer("answer 3", false));
	}

	public String getQuestion() {
		return question;
	}

	public Answer getAnswer1() {
		return answer1;
	}

	public Answer getAnswer2() {
		return answer2;
	}

	public Answer getAnswer3() {
		return answer3;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

}