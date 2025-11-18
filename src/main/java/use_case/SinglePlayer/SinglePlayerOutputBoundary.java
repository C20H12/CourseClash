// huzaifa - present question, present results, error
package use_case.SinglePlayer;

public interface SinglePlayerOutputBoundary {
    void presentQuestion(SinglePlayerOutputData outputData);
    void presentResults(SinglePlayerOutputData outputData);
    void presentError(String message);
}

