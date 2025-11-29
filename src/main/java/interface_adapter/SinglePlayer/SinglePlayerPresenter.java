    // Huzaifa - converts OutputData to view model - Presenter File
    // generated sample code from GPT to be worked on

    package interface_adapter.SinglePlayer;

    import java.util.List;

    import entity.DeckManagement.StudyDeck;
    import use_case.SinglePlayer.SinglePlayerOutputBoundary;
    import use_case.SinglePlayer.SinglePlayerOutputData;

    /**
     * Presenter for the Single Player Game use case.
     * Converts OutputData from the interactor into state updates in the ViewModel.
     */
    public class SinglePlayerPresenter implements SinglePlayerOutputBoundary {

        private final SinglePlayerViewModel viewModel;

        public SinglePlayerPresenter(SinglePlayerViewModel viewModel) {
            this.viewModel = viewModel;
        }

        /**
         * Updates the view model with the latest question data from the interactor.
         * Sets all question-related fields in the SinglePlayerState and fires a
         * property change to notify the view.
         *
         * @param data the output data containing question text, options, score,
         *             progress, and other state information
         */
        @Override
        public void presentQuestion(SinglePlayerOutputData data) {
            SinglePlayerState state = viewModel.getState();
            state.setQuestionText(data.getQuestionText());
            state.setOptions(data.getOptions());
            state.setCurrentIndex(data.getCurrentIndex());
            state.setTotal(data.getTotal());
            state.setScore(data.getScore());
            state.setAccuracy(data.getAccuracy());
            state.setAvgResponseTime(data.getAvgResponseTime());
            state.setGameOver(data.isGameOver());
            state.setMessage(data.getMessage());
            state.setError(null);

            viewModel.setState(state);
            viewModel.firePropertyChange("question");
        }

        /**
         * Updates the view model with the final results of the single-player session.
         * Populates the state with score, accuracy, timing metrics, and game-over
         * information, then triggers a property change for the UI update.
         *
         * @param data the output data containing final results and performance metrics
         */
        @Override
        public void presentResults(SinglePlayerOutputData data) {
            SinglePlayerState state = viewModel.getState();
            state.setScore(data.getScore());
            state.setAccuracy(data.getAccuracy());
            state.setAvgResponseTime(data.getAvgResponseTime());
            state.setMessage(data.getMessage());
            state.setGameOver(true);
            viewModel.firePropertyChange("end");
        }

        /**
         * Updates the view model to display an error state. Sets both the error
         * message and the generic message field, then fires a property change to
         * notify the view that an error has occurred.
         *
         * @param message the error message to display
         */
        @Override
        public void presentError(String message) {
            SinglePlayerState state = viewModel.getState();
            state.setError(message);
            state.setMessage(message);
            viewModel.firePropertyChange("error");
        }

        /**
         * Updates the view model with the list of all available study decks.
         * Populates the SinglePlayerState with the deck names and fires a property
         * change so the view can refresh the deck selection display.
         *
         * @param names the list of decks retrieved from the data access layer
         */
        @Override
        public void presentAllDecks(List<StudyDeck> names) {
            SinglePlayerState state = viewModel.getState();
            state.setDecksList(names);
            viewModel.firePropertyChange("initShowAllDeckNames");
        }
    }
