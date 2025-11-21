//Mahir
package interface_adapter.MultiPlayer.start_match;

import interface_adapter.MultiPlayer.MultiPlayerState;
import interface_adapter.MultiPlayer.MultiPlayerViewModel;
import interface_adapter.ViewManagerModel;
import use_case.MultiPlayer.start_match.MPStartOutputBoundary;
import use_case.MultiPlayer.start_match.MPStartOutputData;

public class MPStartPresenter implements MPStartOutputBoundary {
    private final MultiPlayerViewModel multiPlayerViewModel;
    private final ViewManagerModel viewManagerModel;

    public MPStartPresenter(MultiPlayerViewModel multiPlayerViewModel, ViewManagerModel viewManagerModel) {
        this.multiPlayerViewModel = multiPlayerViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(MPStartOutputData response) {
        MultiPlayerState state = multiPlayerViewModel.getState();
        state.setPlayerA(response.getPlayer1());
        state.setPlayerB(response.getPlayer2());
        state.setCurrentTurnUser(response.getPlayer1());
        state.setCurrentCard(response.getFirstCard());
        state.setMessage("Game Started!");


        multiPlayerViewModel.setState(state);
        multiPlayerViewModel.firePropertyChange();


        viewManagerModel.setState(multiPlayerViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        System.out.println("Error starting multiplayer: " + error);
    }
}