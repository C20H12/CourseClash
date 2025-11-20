package interface_adapter.MultiPlayer;

import interface_adapter.ViewModel;

public class MultiPlayerViewModel extends ViewModel<MultiPlayerState> {

    public static final String TITLE_LABEL = "Multiplayer Match";
    public static final String SUBMIT_BUTTON_LABEL = "Submit Answer";

    public MultiPlayerViewModel() {
        super("multiplayer game");
        setState(new MultiPlayerState());
    }
}