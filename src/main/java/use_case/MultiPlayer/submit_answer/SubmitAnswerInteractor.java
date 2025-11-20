package use_case.MultiPlayer.submit_answer;

import entity.MultiPlayerGame;
import entity.StudyCard;

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
        // 1. Retrieve the game based on the username
        MultiPlayerGame game = userDataAccessObject.getMultiPlayerGame(inputData.getUsername());

        if (game == null) {
            userPresenter.prepareFailView("Game not found for user: " + inputData.getUsername());
            return;
        }

        // 2. Get current logic data
        StudyCard currentCard = game.getCurrentCard();
        String userAnswer = inputData.getAnswer();
        // We assume StudyCard has getAnswer(). If it is getCorrectAnswer(), please adjust this line.
        String correctAnswer = currentCard.getCorrectAnswer();
        String currentTurnUsername = game.getCurrentTurn().getUserName();

        // 3. Check the Answer
        boolean isCorrect = userAnswer.equalsIgnoreCase(correctAnswer);

        // 4. Update Score (Only if correct)
        if (isCorrect) {
            game.incrementScoreFor(currentTurnUsername);
        }

        // 5. Switch Turn
        game.switchTurn();

        // 6. Advance to the next card
        game.advanceCard();

        // 7. Check for Game Over (Implicit End Match)
        boolean isGameOver = game.isGameOver();
        StudyCard nextCard = null;

        if (!isGameOver) {
            nextCard = game.getCurrentCard();
        }

        // 8. Save the updated game state
        userDataAccessObject.save(game);

        // 9. Prepare Output Data
        SubmitAnswerOutputData outputData = new SubmitAnswerOutputData(
                game.getScoreA(),
                game.getScoreB(),
                game.getCurrentTurn().getUserName(),
                nextCard,       // Will be null if game is over
                isCorrect,
                correctAnswer,
                isGameOver      // The flag that tells UI to show "Game Over"
        );

        userPresenter.prepareSuccessView(outputData);
    }
}