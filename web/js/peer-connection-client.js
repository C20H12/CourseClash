async function Java_entity_peer_PeerConnection_nativeSetApplication (
  lib,
  myApplication
) {
  window.myApplication = myApplication;
  console.log("Java application instance set on JavaScript side.");
  // Make the inputDiv visible after initialization
  return new Promise(() => {}); // Keeps the function from returning
}

let peer;
let ID;
let g_conn;
const msgQ = [];
let dataCbReady = false;
let interval = 0;

async function Java_entity_peer_PeerConnection_jsCreatePeer(lib, self, p1) {
  peer = new Peer(p1);

  peer.on('connection', async function(conn) {
    g_conn = conn;
    g_conn.on('open', async function() {
      console.log("recieved connected");
      await (await myApplication.getConnectCallback()).onConnect();
      console.log(111);
      dataCbReady = true;
      // Receive messages
      g_conn.on('data', async function(data) {
        console.log('1223 Received', data);
        await (await myApplication.getDataCallback()).onData(data);
      });
    });
  });
  
  interval = setInterval(() => {
    while (msgQ.length > 0) {
      const msg = msgQ.shift();
      g_conn.send(msg);
    }
  }, 500);
}

async function Java_entity_peer_PeerConnection_jsConnectToPeer(lib, self, p1) {
  console.log(self);
  // console.log(cb);
  // console.log(await cb.onConnect());
  
  g_conn = peer.connect(p1);
  g_conn.on('open', async function() {
    console.log("connected");
    await (await myApplication.getConnectCallback()).onConnect();
    console.log(111);
    dataCbReady = true;
    // Receive messages
    g_conn.on('data', async function(data) {
      console.log('host Received', data);
      await (await myApplication.getDataCallback()).onData(data);
    });
  });
}

async function Java_entity_peer_PeerConnection_jsSendData(lib, self, p1) {
  console.log('sent ' + p1);
  if (!dataCbReady) {
    msgQ.push(p1);
    return;
  }
  g_conn.send(p1);
}

async function Java_entity_peer_PeerConnection_jsDisposePeer(lib, self) {
  console.log("trashing");
  peer = null;
  if (interval) {
    clearInterval(interval);
  }
}

