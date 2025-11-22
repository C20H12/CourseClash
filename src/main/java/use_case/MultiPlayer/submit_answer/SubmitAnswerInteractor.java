package use_case.MultiPlayer.submit_answer;

import entity.MultiPlayerGame;
import entity.DeckManagement.StudyCard;
import use_case.DataAccessException;

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

        // --- SECURITY: Turn Order ---
        if (!inputData.getUsername().equals(game.getCurrentTurn().getUserName())) {
            System.out.println("ILLEGAL MOVE: " + inputData.getUsername() + " tried to play out of turn.");
            return;
        }

        StudyCard currentCard = game.getCurrentCard();
        String userAnswer = inputData.getAnswer();
        String correctAnswer = currentCard.getCorrectAnswer();
        String currentTurnUsername = game.getCurrentTurn().getUserName();

        // 1. Score Update
        boolean isCorrect = userAnswer.equalsIgnoreCase(correctAnswer);
        if (isCorrect) {
            game.incrementScoreFor(currentTurnUsername);
        }

        // 2. Switch Turn (Happens after every single move)
        game.switchTurn();

        // 3. Shared Question Logic
        // Increment counter. If counter == 2, both have played -> Advance Card.
        boolean readyToAdvance = game.recordAnswerAndIsReadyToAdvance();

        if (readyToAdvance) {
            game.advanceCardAndResetCounter();
        }

        // 4. Game Over Check
        boolean isGameOver = game.isGameOver();
        StudyCard nextCard = null;

        if (!isGameOver) {
            nextCard = game.getCurrentCard();
        }

        // 5. Network Sync (Send JSON to opponent)
        userDataAccessObject.save(game);

        // 6. Update Host View
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