package entity.peer;

@FunctionalInterface
public interface PeerConnectCallback {
    /**
     * Callback.
     */
    void onConnect();
}
