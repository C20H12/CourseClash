package interface_adapter.MultiPlayer.submit_answer;

import use_case.MultiPlayer.submit_answer.SubmitAnswerInputBoundary;
import use_case.MultiPlayer.submit_answer.SubmitAnswerInputData;

public class SubmitAnswerController {
    final SubmitAnswerInputBoundary interactor;

    public SubmitAnswerController(SubmitAnswerInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String answer, String username) {
        // Package the data
        SubmitAnswerInputData inputData = new SubmitAnswerInputData(answer, username);
        // Send to Interactor
        interactor.execute(inputData);
    }
}
