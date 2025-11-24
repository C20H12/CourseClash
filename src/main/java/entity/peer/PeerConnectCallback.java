package entity.peer;

@FunctionalInterface
public interface PeerConnectCallback {
  void onConnect() throws Exception;
}
