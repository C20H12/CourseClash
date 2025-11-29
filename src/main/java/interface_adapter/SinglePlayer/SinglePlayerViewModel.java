// Huzaifa - observable state for the view

package interface_adapter.SinglePlayer;

import interface_adapter.ViewModel;

public class SinglePlayerViewModel extends ViewModel<SinglePlayerState> {

    public SinglePlayerViewModel() {
        super("single player");
        setState(new SinglePlayerState());
    }

}
