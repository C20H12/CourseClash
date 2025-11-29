package use_case.MultiPlayer;

import entity.DeckManagement.StudyCard;

/**
 * Data passed from the interactor to the presenter so the view model can be updated.
 */
public class MultiPlayerOutputData {

	private String playerA;
	private String playerB;
	private int scoreA;
	private int scoreB;
	private String message;
	private StudyCard currentCard;
	private String roundResult;
	private boolean gameOver;

	public String getPlayerA() {
		return playerA;
	}

	public void setPlayerA(String playerA) {
		this.playerA = playerA;
	}

	public String getPlayerB() {
		return playerB;
	}

	public void setPlayerB(String playerB) {
		this.playerB = playerB;
	}

	public int getScoreA() {
		return scoreA;
	}

	public void setScoreA(int scoreA) {
		this.scoreA = scoreA;
	}

	public int getScoreB() {
		return scoreB;
	}

	public void setScoreB(int scoreB) {
		this.scoreB = scoreB;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public StudyCard getCurrentCard() {
		return currentCard;
	}

	public void setCurrentCard(StudyCard currentCard) {
		this.currentCard = currentCard;
	}

	public String getRoundResult() {
		return roundResult;
	}

	public void setRoundResult(String roundResult) {
		this.roundResult = roundResult;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
}
