package entity.peer;

@FunctionalInterface
public interface PeerDataCallback {
    /**
     * Callback.
     * @param data data
     */
    void onData(String data);
}
