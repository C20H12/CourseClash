//Mahir
package use_case.MultiPlayer.submit_answer;

public interface SubmitAnswerOutputBoundary {
    void prepareSuccessView(SubmitAnswerOutputData outputData);
    void prepareFailView(String error);
}
