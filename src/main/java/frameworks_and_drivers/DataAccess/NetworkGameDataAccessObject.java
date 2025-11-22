package frameworks_and_drivers.DataAccess;

import entity.MultiPlayerGame;
import entity.peer.PeerConnection;
import use_case.MultiPlayer.MultiPlayerAccessInterface;
import use_case.MultiPlayer.submit_answer.SubmitAnswerDataAccessInterface;

public class NetworkGameDataAccessObject implements MultiPlayerAccessInterface, SubmitAnswerDataAccessInterface {

    private final PeerConnection peerConnection;
    private MultiPlayerGame localGame;

    public NetworkGameDataAccessObject(PeerConnection peerConnection) {
        this.peerConnection = peerConnection;
    }

    @Override
    public void save(MultiPlayerGame game) {
        this.localGame = game;
        // 1. Convert Game to JSON
        String json = GameStateSerializer.serialize(game);
        // 2. Send to Opponent
        peerConnection.sendData(json);
    }

    @Override
    public MultiPlayerGame getMultiPlayerGame(String username) {
        return localGame;
    }

    // Helper to update local state when data arrives from opponent
    public void updateLocalGame(MultiPlayerGame game) {
        this.localGame = game;
    }
}
