package app.quiz.model;

public class Answer {

	private String text;

	private boolean goodAnswer;

	public Answer(String text, boolean goodAnswer) {
		super();
		this.text = text;
		this.goodAnswer = goodAnswer;
	}

	public String getText() {
		return text;
	}

	public boolean isGoodAnswer() {
		return goodAnswer;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setGoodAnswer(boolean goodAnswer) {
		this.goodAnswer = goodAnswer;
	}

}
