// Class to manage WebRTC peer connections
// author: Luhan, Mahir (Refactored for P2P Game Logic)
package entity.peer;

import dev.onvoid.webrtc.*;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

public class PeerConnection {
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

    public PeerConnection(String id) {
        uid = id;
        config = new RTCConfiguration();
        RTCIceServer iceServer = new RTCIceServer();
        iceServer.urls.add("stun:stun.l.google.com:19302");
        config.iceServers.add(iceServer);
        factory = new PeerConnectionFactory();
        initMqtt();
    }

    private void initMqtt() {
        String broker = "tcp://broker.emqx.io:1883";
        String clientId = UUID.randomUUID().toString();
        try {
            client = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            client.connect(options);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public PeerConnection() {
        this(UUID.randomUUID().toString());
    }

    public void switchIdentity(String newId) {
        if (this.uid.equals(newId)) return;

        System.out.println("PEER: Switching identity from " + this.uid + " to " + newId);

        if (peerConnection != null) {
            peerConnection.close();
            peerConnection = null;
        }

        this.uid = newId;

        try {
            client.subscribe(uid + "/get_offer", 2, (topic, msg) -> {
                System.out.println("PEER: Got request for offer on " + uid);
                MqttMessage offerMsg = new MqttMessage(offerJsonObject.toString().getBytes(StandardCharsets.UTF_8));
                offerMsg.setQos(2);
                client.publish(uid + "/offer", offerMsg);
            });
            client.subscribe(uid + "/answer", 2, (topic, msg) -> {
                System.out.println("PEER: Received answer via MQTT on " + uid);
                connect(new String(msg.getPayload(), StandardCharsets.UTF_8));
            });

            createOffer();

            System.out.println("PEER: Identity switched. Listening as " + uid);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void createOffer() {
        System.out.println("Creating offer...");
        offerJsonObject.clear();

        peerConnection = factory.createPeerConnection(config, new PeerConnectionObserver() {
            @Override
            public void onIceCandidate(RTCIceCandidate candidate) {
                if (!offerJsonObject.has("candidates")) {
                    offerJsonObject.put("candidates", new JSONArray());
                }
                JSONArray candidatesJsonArr = offerJsonObject.getJSONArray("candidates");
                JSONObject candidateJsonObj = new JSONObject();
                candidateJsonObj.put("sdpMid", candidate.sdpMid);
                candidateJsonObj.put("sdpMLineIndex", candidate.sdpMLineIndex);
                candidateJsonObj.put("sdp", candidate.sdp);
                candidatesJsonArr.put(candidateJsonObj);
            }

            @Override
            public void onIceGatheringChange(RTCIceGatheringState state) {
                System.out.println("ICE Gathering State: " + state);
            }
        });

        RTCDataChannelInit dataChannelInit = new RTCDataChannelInit();
        gDataChannel = peerConnection.createDataChannel("myDataChannel", dataChannelInit);
        gDataChannel.registerObserver(creatDataChannelObserver(gDataChannel));

        RTCOfferOptions options = new RTCOfferOptions();
        peerConnection.createOffer(options, new CreateSessionDescriptionObserver() {
            @Override
            public void onSuccess(RTCSessionDescription description) {
                peerConnection.setLocalDescription(description, new SetSessionDescriptionObserver() {
                    @Override
                    public void onSuccess() {
                        offerJsonObject.put("sdp", description.sdp);
                    }
                    @Override
                    public void onFailure(String error) { System.err.println("Set Local Desc Failed: " + error); }
                });
            }
            @Override
            public void onFailure(String error) { System.err.println("Create Offer Failed: " + error); }
        });
    }

    private void createAnswer(String offerJsonStr, String targetID) {
        System.out.println("Creating answer...");
        answerJsonObject.clear();

        if (offerJsonStr.isEmpty()) return;

        JSONObject offerJsonObj = new JSONObject(offerJsonStr);
        String offerSdp = offerJsonObj.getString("sdp");
        JSONArray candidateJsonArr = offerJsonObj.getJSONArray("candidates");
        ArrayList<RTCIceCandidate> remoteCandidates = new ArrayList<>();

        for (int i = 0; i < candidateJsonArr.length(); i++) {
            JSONObject candidateJsonObj = candidateJsonArr.getJSONObject(i);
            RTCIceCandidate candidate = new RTCIceCandidate(
                    candidateJsonObj.getString("sdpMid"),
                    candidateJsonObj.getInt("sdpMLineIndex"),
                    candidateJsonObj.getString("sdp")
            );
            remoteCandidates.add(candidate);
        }

        peerConnection = factory.createPeerConnection(config, new PeerConnectionObserver() {
            @Override
            public void onIceCandidate(RTCIceCandidate candidate) {
                if (!answerJsonObject.has("candidates")) {
                    answerJsonObject.put("candidates", new JSONArray());
                }
                JSONObject candidateJsonObj = new JSONObject();
                candidateJsonObj.put("sdpMid", candidate.sdpMid);
                candidateJsonObj.put("sdpMLineIndex", candidate.sdpMLineIndex);
                candidateJsonObj.put("sdp", candidate.sdp);
                answerJsonObject.getJSONArray("candidates").put(candidateJsonObj);
            }

            @Override
            public void onDataChannel(RTCDataChannel dataChannel) {
                gDataChannel = dataChannel;
                gDataChannel.registerObserver(creatDataChannelObserver(gDataChannel));
                System.out.println("Data channel received: " + dataChannel.getLabel());
            }

            @Override
            public void onIceGatheringChange(RTCIceGatheringState state) {
                if (state == RTCIceGatheringState.COMPLETE) {
                    try {
                        MqttMessage answerMsg = new MqttMessage(answerJsonObject.toString().getBytes(StandardCharsets.UTF_8));
                        answerMsg.setQos(2);
                        client.publish(targetID + "/answer", answerMsg);
                    } catch (MqttException e) { e.printStackTrace(); }
                }
            }
        });

        RTCSessionDescription offerDescription = new RTCSessionDescription(RTCSdpType.OFFER, offerSdp);
        peerConnection.setRemoteDescription(offerDescription, new SetSessionDescriptionObserver() {
            @Override
            public void onSuccess() {
                for (RTCIceCandidate candidate : remoteCandidates) {
                    peerConnection.addIceCandidate(candidate);
                }
                peerConnection.createAnswer(new RTCAnswerOptions(), new CreateSessionDescriptionObserver() {
                    @Override
                    public void onSuccess(RTCSessionDescription answerDescription) {
                        peerConnection.setLocalDescription(answerDescription, new SetSessionDescriptionObserver() {
                            @Override
                            public void onSuccess() {
                                answerJsonObject.put("sdp", answerDescription.sdp);
                            }
                            @Override
                            public void onFailure(String error) {}
                        });
                    }
                    @Override
                    public void onFailure(String error) {}
                });
            }
            @Override
            public void onFailure(String error) {}
        });
    }

    private void connect(String answerJsonStr) {
        if (peerConnection == null) return;

        JSONObject answerJsonObj = new JSONObject(answerJsonStr);
        String answerSdp = answerJsonObj.getString("sdp");
        JSONArray candidateJsonArr = answerJsonObj.getJSONArray("candidates");

        RTCSessionDescription answerDescription = new RTCSessionDescription(RTCSdpType.ANSWER, answerSdp);
        peerConnection.setRemoteDescription(answerDescription, new SetSessionDescriptionObserver() {
            @Override
            public void onSuccess() {
                System.out.println("Remote description (answer) set successfully");
                for (int i = 0; i < candidateJsonArr.length(); i++) {
                    JSONObject candidateJsonObj = candidateJsonArr.getJSONObject(i);
                    RTCIceCandidate candidate = new RTCIceCandidate(
                            candidateJsonObj.getString("sdpMid"),
                            candidateJsonObj.getInt("sdpMLineIndex"),
                            candidateJsonObj.getString("sdp")
                    );
                    peerConnection.addIceCandidate(candidate);
                }
            }
            @Override
            public void onFailure(String error) { System.out.println("Failed to set remote description: " + error); }
        });
    }

    private RTCDataChannelObserver creatDataChannelObserver(RTCDataChannel channel) {
        return new RTCDataChannelObserver() {
            @Override
            public void onBufferedAmountChange(long previousAmount) {}
            @Override
            public void onStateChange() {
                if (channel.getState() == RTCDataChannelState.OPEN) {
                    System.out.println("Data channel is open!");
                    if (connectCallback != null) {
                        try {
                            connectCallback.onConnect();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            @Override
            public void onMessage(RTCDataChannelBuffer buffer) {
                ByteBuffer data = buffer.data;
                byte[] textBytes = new byte[data.remaining()];
                data.get(textBytes);
                String message = new String(textBytes, StandardCharsets.UTF_8);
                if (dataCallback != null) dataCallback.onData(message);
            }
        };
    }

    public void onDataRecieved(PeerDataCallback cb) { dataCallback = cb; }

    public void connectToPeer(String targetId, PeerConnectCallback cb) {
        connectCallback = cb;
        try {
            client.subscribe(targetId + "/offer", 2, (topic, msg) -> {
                createAnswer(new String(msg.getPayload(), StandardCharsets.UTF_8), targetId);
            });
            MqttMessage reqMsg = new MqttMessage("req".getBytes(StandardCharsets.UTF_8));
            reqMsg.setQos(2);
            client.publish(targetId + "/get_offer", reqMsg);
        } catch (MqttException e) { e.printStackTrace(); }
    }

    public void onConnection(PeerConnectCallback cb) {
        connectCallback = cb;
        createOffer();
        try {
            client.subscribe(uid + "/get_offer", 2, (topic, msg) -> {
                MqttMessage offerMsg = new MqttMessage(offerJsonObject.toString().getBytes(StandardCharsets.UTF_8));
                offerMsg.setQos(2);
                client.publish(uid + "/offer", offerMsg);
            });
            client.subscribe(uid + "/answer", 2, (topic, msg) -> {
                connect(new String(msg.getPayload(), StandardCharsets.UTF_8));
            });
        } catch (MqttException e) { e.printStackTrace(); }
    }

    public void sendData(String message) throws Exception {
        if (gDataChannel != null && gDataChannel.getState() == RTCDataChannelState.OPEN) {
            ByteBuffer messaBuffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
            RTCDataChannelBuffer messagDataChannelBuffer = new RTCDataChannelBuffer(messaBuffer, false);
            gDataChannel.send(messagDataChannelBuffer);
        }
    }

    public String getUid() { return uid; }

    public void dispose() {
        if (peerConnection != null) peerConnection.close();
        if (client != null) {
            try {
                client.disconnect();
                client.close();
            } catch (MqttException e) { e.printStackTrace(); }
        }
    }
}