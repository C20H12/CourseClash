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
const msgQRecQ = [];
let dataCbReady = false;
let interval = 0;

async function Java_entity_peer_PeerConnection_jsCreatePeer(lib, self, p1) {
  peer = new Peer(p1);

  peer.on('connection', async function(conn) {
    g_conn = conn;
    g_conn.on('open', async function() {
      // Receive messages
      g_conn.on('data', async function(data) {
        console.log('1223 Received', data);
        if (!dataCbReady) {
          msgQRecQ.push(data);
          return;
        }
        dataCbReady = false;
        await (await myApplication.getDataCallback()).onData(data);
        dataCbReady = true;
      });
      console.log("recieved connected");
      dataCbReady = false;
      await (await myApplication.getConnectCallback()).onConnect();
      dataCbReady = true;
      console.log(111);
    });
  });
  
  interval = setInterval(async () => {
    if (!dataCbReady) return;
    while (msgQ.length > 0) {
      const msg = msgQ.shift();
      g_conn.send(msg);
    }
    while (msgQRecQ.length > 0) {
      const msg = msgQRecQ.shift();
      dataCbReady = false;
      await (await myApplication.getDataCallback()).onData(msg);
      dataCbReady = true;
    }
  }, 500);
}

async function Java_entity_peer_PeerConnection_jsConnectToPeer(lib, self, p1) {
  console.log(self);
  // console.log(cb);
  // console.log(await cb.onConnect());
  
  g_conn = peer.connect(p1);
  g_conn.on('open', async function() {
    // Receive messages
    g_conn.on('data', async function(data) {
      console.log('host Received', data);
      if (!dataCbReady) {
        msgQRecQ.push(data);
        return;
      }
      dataCbReady = false;
      await (await myApplication.getDataCallback()).onData(data);
      dataCbReady = true;
    });
    console.log("connected");
    dataCbReady = false;
    await (await myApplication.getConnectCallback()).onConnect();
    dataCbReady = true;
    console.log(111);
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

