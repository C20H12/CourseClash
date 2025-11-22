package frameworks_and_drivers.DataAccess;

import entity.MultiPlayerGame;
import entity.peer.PeerConnection;
import use_case.MultiPlayer.MultiPlayerAccessInterface;
import use_case.MultiPlayer.submit_answer.SubmitAnswerDataAccessInterface;

public class NetworkGameDataAccessObject implements MultiPlayerAccessInterface, SubmitAnswerDataAccessInterface {

    private final PeerConnection peerConnection;
    private MultiPlayerGame localGame;
    private boolean isConnected = false;
    private boolean isConnecting = false;

    public NetworkGameDataAccessObject(PeerConnection peerConnection) {
        this.peerConnection = peerConnection;
    }

    @Override
    public void save(MultiPlayerGame game) {
        this.localGame = game;
        try {
            String jsonState = GameStateSerializer.serialize(game);

            System.out.println("NETWORK DAO: Attempting to save/send game state...");

            if (isConnected) {
                System.out.println("NETWORK DAO: Sending data via open channel...");
                peerConnection.sendData(jsonState);
                return;
            }

            if (isConnecting) {
                System.out.println("NETWORK DAO: Connection busy, skipping send.");
                return;
            }

            String currentUser = peerConnection.getUid();
            String opponent = game.getPlayerA().getUserName().equals(currentUser)
                    ? game.getPlayerB().getUserName()
                    : game.getPlayerA().getUserName();

            isConnecting = true;
            System.out.println("NETWORK DAO: Establishing connection to " + opponent);

            peerConnection.connectToPeer(opponent, () -> {
                System.out.println("NETWORK DAO: Connection Established! Sending initial state.");
                isConnected = true;
                isConnecting = false;
                peerConnection.sendData(jsonState);
            });

        } catch (Exception e) {
            System.out.println("NETWORK DAO ERROR: " + e.getMessage());
            e.printStackTrace();
            isConnecting = false;
        }
    }

    @Override
    public MultiPlayerGame getMultiPlayerGame(String username) {
        return localGame;
    }

    public void updateLocalGame(MultiPlayerGame game) {
        this.localGame = game;
        this.isConnected = true;
        System.out.println("NETWORK DAO: Local state updated from remote message.");
    }
}