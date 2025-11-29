//Mahir

package interface_adapter.MultiPlayer;

import interface_adapter.ViewModel;

/**
 * ViewModel for the Multiplayer game mode.
 * Manages the state and properties required for the multiplayer view.
 */
public class MultiPlayerViewModel extends ViewModel<MultiPlayerGameState> {

    /**
     * The title text displayed on the multiplayer screen.
     */
    public static final String TITLE_LABEL = "Multiplayer Match";

    /**
     * The text label for the answer submission button.
     */
    public static final String SUBMIT_BUTTON_LABEL = "Submit Answer";

    /**
     * Constructs the MultiPlayerViewModel, initializing the view name and default state.
     */
    public MultiPlayerViewModel() {
        super("multiplayer game");
        setState(new MultiPlayerGameState());
    }
}