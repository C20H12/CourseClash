package use_case.MultiPlayer.start_match;

public interface MPStartOutputBoundary {
    void prepareSuccessView(MPStartOutputData outputData);
    void prepareFailView(String message);
}
