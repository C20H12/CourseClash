package use_case.MultiPlayer.submit_answer;

import entity.MultiPlayerGame;
import entity.DeckManagement.StudyCard;

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
            userPresenter.prepareFailView("Game not found for user: " + inputData.getUsername());
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
                game.getScoreA(), game.getScoreB(), game.getCurrentTurn().getUserName(),
                nextCard, isCorrect, correctAnswer, isGameOver
        );

        userPresenter.prepareSuccessView(outputData);
    }
}