// Class to manage WebRTC peer connections
// author: Luhan

package entity.peer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONArray;
import org.json.JSONObject;

import dev.onvoid.webrtc.CreateSessionDescriptionObserver;
import dev.onvoid.webrtc.PeerConnectionFactory;
import dev.onvoid.webrtc.PeerConnectionObserver;
import dev.onvoid.webrtc.RTCAnswerOptions;
import dev.onvoid.webrtc.RTCConfiguration;
import dev.onvoid.webrtc.RTCDataChannel;
import dev.onvoid.webrtc.RTCDataChannelBuffer;
import dev.onvoid.webrtc.RTCDataChannelInit;
import dev.onvoid.webrtc.RTCDataChannelObserver;
import dev.onvoid.webrtc.RTCDataChannelState;
import dev.onvoid.webrtc.RTCIceCandidate;
import dev.onvoid.webrtc.RTCIceGatheringState;
import dev.onvoid.webrtc.RTCIceServer;
import dev.onvoid.webrtc.RTCOfferOptions;
import dev.onvoid.webrtc.RTCPeerConnection;
import dev.onvoid.webrtc.RTCSdpType;
import dev.onvoid.webrtc.RTCSessionDescription;
import dev.onvoid.webrtc.SetSessionDescriptionObserver;

public class PeerConnection {
  private static final int MQTTTIMEOUT = 10;

  private String uid;

  private RTCConfiguration config;
  private PeerConnectionFactory factory;
  private RTCPeerConnection peerConnection;
  private RTCDataChannel gDataChannel;

  private JSONObject offerJsonObject = new JSONObject();
  private JSONObject answerJsonObject = new JSONObject();

  private MqttClient client;

  private PeerConnectCallback connectCallback;
  private PeerDataCallback dataCallback;

  private Logger logger;

  /**
   * Creates a new PeerConnection with the specified ID.
   * Initializes WebRTC configuration with ICE servers and connects to MQTT
   * broker.
   * 
   * @param id the unique identifier for this peer connection
   */
  public PeerConnection(String id) {
    uid = id;

    // Configure ICE servers (STUN/TURN)
    config = new RTCConfiguration();
    final RTCIceServer iceServer = new RTCIceServer();
    iceServer.urls.add("stun:stun.l.google.com:19302");
    iceServer.urls.add("stun:stun.l.google.com:19302");
    iceServer.urls.add("stun:stun.l.google.com:5349");
    iceServer.urls.add("stun:stun1.l.google.com:3478");
    config.iceServers.add(iceServer);

    // Create a peer connection factory
    factory = new PeerConnectionFactory();

    // connect to MQTT broker
    final String broker = "tcp://broker.emqx.io:1883";
    final String clientId = UUID.randomUUID().toString();
    try {
      client = new MqttClient(broker, clientId, new MemoryPersistence());
      final MqttConnectOptions options = new MqttConnectOptions();
      options.setAutomaticReconnect(true);
      options.setCleanSession(true);
      options.setConnectionTimeout(MQTTTIMEOUT);
      client.connect(options);
    }
    catch (MqttException e) {
      e.printStackTrace();
    }

    logger = Logger.getLogger(PeerConnection.class.getName());
    logger.setUseParentHandlers(false);
    final FileHandler fh;
    try {
      final String path = "\\src\\main\\resources\\logs\\peer_connection_log.txt";
      final String pwd = Path.of("").toAbsolutePath().toString();
      fh = new FileHandler(pwd + path);
      logger.addHandler(fh);
      final SimpleFormatter formatter = new SimpleFormatter();
      fh.setFormatter(formatter);
    }
    catch (SecurityException | IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Creates a new PeerConnection with a randomly generated UUID as the ID.
   */
  public PeerConnection() {
    this(UUID.randomUUID().toString());
  }

  /**
   * Creates a WebRTC offer for establishing a peer connection.
   * Initializes the peer connection, creates a data channel, and generates ICE
   * candidates.
   */
  private void createOffer() {
    logPeerConncetionInfo("Creating offer...");

    // Clear previous
    offerJsonObject.clear();

    // step 1: Create a peer connection with an observer to handle events
    peerConnection = factory.createPeerConnection(config, new PeerConnectionObserver() {
      @Override
      public void onIceCandidate(RTCIceCandidate candidate) {
        // step 5: generate candidates
        logPeerConncetionInfo("ICE Candidate for offer generated: " + candidate.sdpMid);

        if (!offerJsonObject.has("candidates")) {
          offerJsonObject.put("candidates", new JSONArray());
        }
        final JSONArray candidatesJsonArr = offerJsonObject.getJSONArray("candidates");
        final JSONObject candidateJsonObj = new JSONObject();
        candidateJsonObj.put("sdpMid", candidate.sdpMid);
        candidateJsonObj.put("sdpMLineIndex", candidate.sdpMLineIndex);
        candidateJsonObj.put("sdp", candidate.sdp);
        candidatesJsonArr.put(candidateJsonObj);
      }

      @Override
      public void onIceGatheringChange(RTCIceGatheringState state) {
        PeerConnectionObserver.super.onIceGatheringChange(state);
        // step 4.1: GATHERING
        // step 6: COMPLETE
        logPeerConncetionInfo("ICE Gathering State: " + state);
      }
    });

    logPeerConncetionInfo("Peer connection created");

    // step 2: Create a data channel to trigger ICE gathering
    final RTCDataChannelInit dataChannelInit = new RTCDataChannelInit();
    gDataChannel = peerConnection.createDataChannel("myDataChannel", dataChannelInit);
    gDataChannel.registerObserver(creatDataChannelObserver(gDataChannel));
    logPeerConncetionInfo("Data channel created: " + gDataChannel.getLabel());

    // step 3: Create an offer
    final RTCOfferOptions options = new RTCOfferOptions();

    peerConnection.createOffer(options, new CreateSessionDescriptionObserver() {
      @Override
      public void onSuccess(RTCSessionDescription description) {
        logPeerConncetionInfo("Offer created successfully");
        // step 4: Set local description
        peerConnection.setLocalDescription(description, new SetSessionDescriptionObserver() {
          @Override
          public void onSuccess() {
            // save the SDP first, candidates will be appended as they arrive
            logPeerConncetionInfo("Local description set - ICE gathering started");
            offerJsonObject.put("sdp", description.sdp);
          }

          @Override
          public void onFailure(String error) {
            logPeerConncetionInfo("Failed to set local description: " + error);
          }
        });
      }

      @Override
      public void onFailure(String error) {
        logPeerConncetionInfo("Failed to create offer: " + error);
      }
    });
  }

  /**
   * Creates a WebRTC answer in response to an offer from another peer.
   * Parses the offer, sets up the peer connection, and generates an answer with
   * ICE candidates.
   * 
   * @param offerJsonStr the JSON string containing the offer SDP and ICE
   *                     candidates
   * @param targetID     the ID of the peer that sent the offer
   */
  private void createAnswer(String offerJsonStr, String targetID) {
    logPeerConncetionInfo("Creating answer...");

    answerJsonObject.clear();

    // step 0: Get the offer data
    final String offerData = offerJsonStr;

    if (offerData.isEmpty()) {
      logPeerConncetionInfo("Error: Offer empty!");
      return;
    }

    // Parse the offer data
    final JSONObject offerJsonObj = new JSONObject(offerData);
    final String offerSdp = offerJsonObj.getString("sdp");
    final JSONArray candidateJsonArr = offerJsonObj.getJSONArray("candidates");
    final ArrayList<RTCIceCandidate> remoteCandidates = new ArrayList<>();

    for (int i = 0; i < candidateJsonArr.length(); i++) {
      final JSONObject candidateJsonObj = candidateJsonArr.getJSONObject(i);
      final String sdpMid = candidateJsonObj.getString("sdpMid");
      final int sdpMLineIndex = candidateJsonObj.getInt("sdpMLineIndex");
      final String sdp = candidateJsonObj.getString("sdp");
      final RTCIceCandidate candidate = new RTCIceCandidate(sdpMid, sdpMLineIndex, sdp);
      remoteCandidates.add(candidate);
    }

    logPeerConncetionInfo(
        "Parsed SDP (" + offerSdp.length() + " chars) and " + candidateJsonArr.length() + " candidates");

    // step 1: Create a peer connection with an observer to handle events
    peerConnection = factory.createPeerConnection(config, new PeerConnectionObserver() {
      @Override
      public void onIceCandidate(RTCIceCandidate candidate) {
        // step 6: generate candidates for this peer, the answerer
        logPeerConncetionInfo("ICE Candidate generated for answer: " + candidate.sdpMid);

        if (!answerJsonObject.has("candidates")) {
          answerJsonObject.put("candidates", new JSONArray());
        }
        final JSONArray candidatesJsonArr = answerJsonObject.getJSONArray("candidates");
        final JSONObject candidateJsonObj = new JSONObject();
        candidateJsonObj.put("sdpMid", candidate.sdpMid);
        candidateJsonObj.put("sdpMLineIndex", candidate.sdpMLineIndex);
        candidateJsonObj.put("sdp", candidate.sdp);
        candidatesJsonArr.put(candidateJsonObj);
      }

      @Override
      public void onDataChannel(RTCDataChannel dataChannel) {
        // step 8: recieve data channel, this is after the connect action
        gDataChannel = dataChannel;
        gDataChannel.registerObserver(creatDataChannelObserver(gDataChannel));
        logPeerConncetionInfo("Data channel received: " + dataChannel.getLabel());
      }

      @Override
      public void onIceGatheringChange(RTCIceGatheringState state) {
        PeerConnectionObserver.super.onIceGatheringChange(state);
        // step 5.1: GATHERING
        // step 7: COMPLETE
        logPeerConncetionInfo("ICE Gathering State: " + state);

        if (state == RTCIceGatheringState.COMPLETE) {
          final String targetConnectionId = targetID;
          try {
            final MqttMessage answerMsg = new MqttMessage(answerJsonObject.toString().getBytes(StandardCharsets.UTF_8));
            answerMsg.setQos(2);
            client.publish(targetConnectionId + "/answer", answerMsg);
          } catch (MqttException e) {
            e.printStackTrace();
          }
        }
      }
    });

    logPeerConncetionInfo("Peer connection created for answer");

    // also step 0: parsing the offer then Create RTCSessionDescription from the
    // offer
    final RTCSessionDescription offerDescription = new RTCSessionDescription(RTCSdpType.OFFER, offerSdp);

    // step 2: Set the remote description (the offer)
    peerConnection.setRemoteDescription(offerDescription, new SetSessionDescriptionObserver() {
      @Override
      public void onSuccess() {
        logPeerConncetionInfo("Remote description (offer) set successfully");

        // step 3: Add all the remote ICE candidates that the offer created
        for (RTCIceCandidate candidate : remoteCandidates) {
          peerConnection.addIceCandidate(candidate);
          logPeerConncetionInfo("Added remote ICE candidate: " + candidate.sdpMid);
        }

        // step 4: Create an answer
        peerConnection.createAnswer(new RTCAnswerOptions(), new CreateSessionDescriptionObserver() {
          @Override
          public void onSuccess(RTCSessionDescription answerDescription) {
            logPeerConncetionInfo("Answer created successfully");

            // step 5: Set local description (the answer)
            peerConnection.setLocalDescription(answerDescription, new SetSessionDescriptionObserver() {
              @Override
              public void onSuccess() {
                logPeerConncetionInfo("Local description (answer) set - ICE gathering started");

                // Display the answer in the answer text area
                answerJsonObject.put("sdp", answerDescription.sdp);
              }

              @Override
              public void onFailure(String error) {
                logPeerConncetionInfo("Failed to set local description: " + error);
              }
            });
          }

          @Override
          public void onFailure(String error) {
            logPeerConncetionInfo("Failed to create answer: " + error);
          }
        });
      }

      @Override
      public void onFailure(String error) {
        logPeerConncetionInfo("Failed to set remote description: " + error);
      }
    });
  }

  /**
   * Establishes the connection by processing the answer from the remote peer.
   * Parses the answer SDP and ICE candidates, then completes the connection
   * setup.
   * 
   * @param answerJsonStr the JSON string containing the answer SDP and ICE
   *                      candidates
   */
  private void connect(String answerJsonStr) {
    if (peerConnection == null) {
      logPeerConncetionInfo("Peer connection not initialized.");
      return;
    }

    // parse the returned answer data
    final JSONObject answerJsonObj = new JSONObject(answerJsonStr);
    final String answerSdp = answerJsonObj.getString("sdp");
    final JSONArray candidateJsonArr = answerJsonObj.getJSONArray("candidates");
    final ArrayList<RTCIceCandidate> remoteCandidates = new ArrayList<>();

    for (int i = 0; i < candidateJsonArr.length(); i++) {
      final JSONObject candidateJsonObj = candidateJsonArr.getJSONObject(i);
      final String sdpMid = candidateJsonObj.getString("sdpMid");
      final int sdpMLineIndex = candidateJsonObj.getInt("sdpMLineIndex");
      final String sdp = candidateJsonObj.getString("sdp");
      final RTCIceCandidate candidate = new RTCIceCandidate(sdpMid, sdpMLineIndex, sdp);
      remoteCandidates.add(candidate);
      logPeerConncetionInfo("Parsed ICE candidate: " + candidate.sdpMid);
    }

    System.out
        .println("Parsed SDP (" + answerSdp.length() + " chars) and " + candidateJsonArr.length() + " candidates");

    final RTCSessionDescription answerDescription = new RTCSessionDescription(RTCSdpType.ANSWER, answerSdp);
    peerConnection.setRemoteDescription(answerDescription, new SetSessionDescriptionObserver() {
      @Override
      public void onSuccess() {
        logPeerConncetionInfo("Remote description (answer) set successfully");

        // Add all the remote ICE candidates
        for (RTCIceCandidate candidate : remoteCandidates) {
          peerConnection.addIceCandidate(candidate);
          logPeerConncetionInfo("Added remote ICE candidate: " + candidate.sdpMid);
        }
      }

      @Override
      public void onFailure(String error) {
        logPeerConncetionInfo("Failed to set remote description (answer): " + error);
      }
    });
  }

  /**
   * Creates an observer for monitoring data channel events.
   * Handles state changes and incoming messages on the data channel.
   * 
   * @param channel the RTCDataChannel to observe
   * @return an RTCDataChannelObserver configured to handle channel events
   */
  private RTCDataChannelObserver creatDataChannelObserver(RTCDataChannel channel) {
    return new RTCDataChannelObserver() {
      @Override
      public void onBufferedAmountChange(long previousAmount) {
      }

      @Override
      public void onStateChange() {
        // last step for both: when this is OPEN, the connection is established
        if (channel.getState() == RTCDataChannelState.OPEN) {
          logPeerConncetionInfo("Data channel is open: " + channel.getLabel());
          if (connectCallback != null) {
            connectCallback.onConnect();
          }
        } else {
          logPeerConncetionInfo("Data channel state changed: " + channel.getState());
        }
      }

      @Override
      public void onMessage(RTCDataChannelBuffer buffer) {
        final ByteBuffer data = buffer.data;
        final byte[] textBytes;

        if (data.hasArray()) {
          textBytes = data.array();
        } else {
          textBytes = new byte[data.remaining()];
          data.get(textBytes);
        }

        final String message = new String(textBytes, StandardCharsets.UTF_8);
        dataCallback.onData(message);
      }

    };
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
    try {
      // need to subscribe first or else the offer might return very fast
      client.subscribe(targetId + "/offer", 2, (topic, msg) -> {
        logPeerConncetionInfo("Received offer via MQTT");
        createAnswer(new String(msg.getPayload(), StandardCharsets.UTF_8), targetId);
      });
      logPeerConncetionInfo("getting offer");
      final MqttMessage reqMsg = new MqttMessage("req".getBytes(StandardCharsets.UTF_8));
      reqMsg.setQos(2);
      client.publish(targetId + "/get_offer", reqMsg);
    } catch (MqttPersistenceException e) {
      logPeerConncetionInfo("Error in auto-connect: " + e.getMessage());
      e.printStackTrace();
    } catch (MqttException e) {
      logPeerConncetionInfo("Error in auto-connect: " + e.getMessage());
      e.printStackTrace();
    }
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
    createOffer();
    try {
      logPeerConncetionInfo("Host init finished, code: " + uid);
      client.subscribe(uid + "/get_offer", 2, (topic, msg) -> {
        logPeerConncetionInfo("got request for offer");
        final MqttMessage offerMsg = new MqttMessage(offerJsonObject.toString().getBytes(StandardCharsets.UTF_8));
        offerMsg.setQos(2);
        client.publish(uid + "/offer", offerMsg);
      });
      client.subscribe(uid + "/answer", 2, (topic, msg) -> {
        logPeerConncetionInfo("Received answer via MQTT");
        connect(new String(msg.getPayload(), StandardCharsets.UTF_8));
      });
    } catch (MqttException e) {
      logPeerConncetionInfo("Error in auto-connect: " + e.getMessage());
    }
  }

  /**
   * Sends a text message to the connected peer through the data channel.
   * 
   * @param message the text message to send
   */
  public void sendData(String message) {
    if (gDataChannel != null && gDataChannel.getState() == RTCDataChannelState.OPEN) {
      final ByteBuffer messaBuffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
      final RTCDataChannelBuffer messagDataChannelBuffer = new RTCDataChannelBuffer(messaBuffer, false);
      try {
        gDataChannel.send(messagDataChannelBuffer);
        logPeerConncetionInfo("Sent message over data channel: " + message);
        //CHECKSTYLE:OFF
      } catch (Exception e) {
        //CHECKSTYLE:ON
        logPeerConncetionInfo("Failed to send message over data channel: " + e.getMessage());
      }
    } else {
      logPeerConncetionInfo("Data channel is not open. Cannot send message.");
    }
  }

  private void logPeerConncetionInfo(String string) {
    // log to src\main\resources\logs\peer_connection_log.txt
    logger.info(string);
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
    if (peerConnection != null) {
      peerConnection.close();
    }
    if (client != null) {
      try {
        client.disconnect();
        client.close();
      } catch (MqttException e1) {
        e1.printStackTrace();
      }
    }
  }

  // public static void main(String[] args) {
  // // test out this class
  // PeerConnection pc = new PeerConnection();
  // PeerConnection pc1 = new PeerConnection();
  // pc.onConnection(() -> {
  // logPeerConncetionInfo("----Connected to peer!");
  // pc.sendData("-------Hello from peer!");
  // });
  // pc.onDataRecieved((data) -> {
  // logPeerConncetionInfo("------Received data: " + data);
  // });
  // pc1.connectToPeer(pc.uid, () -> {
  // logPeerConncetionInfo("------Connected to peer 0!");
  // pc1.sendData("-------Hello from peer1!");
  // });
  // pc1.onDataRecieved((data) -> {
  // logPeerConncetionInfo("------Received data: " + data);
  // });
  // // pc.dispose();
  // // pc1.dispose();
  // }
}