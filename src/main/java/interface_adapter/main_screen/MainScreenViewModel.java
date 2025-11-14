package interface_adapter.main_screen;

import interface_adapter.ViewModel;

public class MainScreenViewModel extends ViewModel<LoggedInState> {
    public MainScreenViewModel() {
        super("main screen");
        setState(new LoggedInState());
    }
}
