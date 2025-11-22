//Mahir
package use_case.MultiPlayer.submit_answer;

import entity.DeckManagement.StudyCard;
import entity.MultiPlayerGame;

public class SubmitAnswerInteractor implements SubmitAnswerInputBoundary {
    private final SubmitAnswerDataAccessInterface userDataAccessObject;
    private final SubmitAnswerOutputBoundary userPresenter;

    public SubmitAnswerInteractor(SubmitAnswerDataAccessInterface userDataAccessObject,
                                  SubmitAnswerOutputBoundary userPresenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.userPresenter = userPresenter;
    }

    @Override
    public void execute(SubmitAnswerInputData inputData) {
        MultiPlayerGame game = userDataAccessObject.getMultiPlayerGame(inputData.getUsername());

        if (game == null) {
            System.out.println("Error: Game not found for " + inputData.getUsername());
            return;
        }

        if (!inputData.getUsername().equals(game.getCurrentTurn().getUserName())) {
            System.out.println("ILLEGAL MOVE: " + inputData.getUsername() + " tried to play out of turn.");
            return;
        }

        StudyCard currentCard = game.getCurrentCard();
        String userAnswer = inputData.getAnswer();
        String correctAnswer = currentCard.getCorrectAnswer();
        String currentTurnUsername = game.getCurrentTurn().getUserName();

        boolean isCorrect = userAnswer.equalsIgnoreCase(correctAnswer);
        if (isCorrect) {
            game.incrementScoreFor(currentTurnUsername);
        }

        game.switchTurn();

        boolean readyToAdvance = game.recordAnswerAndIsReadyToAdvance();

        if (readyToAdvance) {
            game.advanceCardAndResetCounter();
        }

        boolean isGameOver = game.isGameOver();
        StudyCard nextCard = null;

        if (!isGameOver) {
            nextCard = game.getCurrentCard();
        }

        userDataAccessObject.save(game);

        SubmitAnswerOutputData outputData = new SubmitAnswerOutputData(
                game.getScoreA(),
                game.getScoreB(),
                game.getCurrentTurn().getUserName(),
                nextCard,
                isCorrect,
                correctAnswer,
                isGameOver,
                game.getPlayerA().getUserName(),
                game.getPlayerB().getUserName()
        );

        userPresenter.prepareSuccessView(outputData);
    }
}