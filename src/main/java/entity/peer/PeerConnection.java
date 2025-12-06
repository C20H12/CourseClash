// Class to manage WebRTC peer connections
// author: Luhan

package entity.peer;

import java.util.UUID;


public class PeerConnection {
    private Thread jsThread;

    private String uid;
    private PeerDataCallback dataCallback;
    public PeerDataCallback getDataCallback() {
      return dataCallback;
    }

    private PeerConnectCallback connectCallback;
    public PeerConnectCallback getConnectCallback() {
      return connectCallback;
    }
    private native void jsCreatePeer(String uid);
    private native void jsSendData(String msg);
    private native void jsConnectToPeer(String uid);
    private native void jsDisposePeer();

    public static native void nativeSetApplication(PeerConnection myApplication);


    /**
     * Creates a new PeerConnection with the specified ID.
     * Initializes WebRTC configuration with ICE servers and connects to MQTT
     * broker.
     *
     * @param id the unique identifier for this peer connection
     */
    public PeerConnection(String id) {
        uid = id;
        jsCreatePeer(uid);
        jsThread = new Thread(() -> {
            nativeSetApplication(this);
        });
        jsThread.start();
    }

    /**
     * Creates a new PeerConnection with a randomly generated UUID as the ID.
     */
    public PeerConnection() {
        this(UUID.randomUUID().toString());
    }


    /**
     * Registers a callback to be invoked when data is received from the peer.
     *
     * @param callBack the callback to handle incoming data
     */
    public void onDataRecieved(PeerDataCallback callBack) {
        dataCallback = callBack;
    }

    /**
     * Initiates a connection to another peer identified by the target ID.
     * Subscribes to MQTT topics and requests an offer from the target peer.
     *
     * @param targetId the unique identifier of the peer to connect to
     * @param callBack       the callback to be invoked when the connection is established
     */
    public void connectToPeer(String targetId, PeerConnectCallback callBack) {
        connectCallback = callBack;
        jsConnectToPeer(targetId);
    }

    /**
     * Sets up this peer as a host ready to accept incoming connections.
     * Creates an offer and subscribes to MQTT topics for offer requests and
     * answers.
     *
     * @param callBack the callback to be invoked when a peer connects
     */
    public void onConnection(PeerConnectCallback callBack) {
        connectCallback = callBack;

    }

    /**
     * Sends a text message to the connected peer through the data channel.
     *
     * @param message the text message to send
     */
    public void sendData(String message) {
      jsSendData(message);
    }

    /**
     * Gets the unique identifier of this peer connection.
     * @return String uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * Cleans up resources by closing the peer connection and disconnecting from
     * MQTT broker.
     * Should be called when the connection is no longer needed.
     */
    public void dispose() {
      jsThread.interrupt();
      jsDisposePeer();
    }
}