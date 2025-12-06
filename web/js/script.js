const peer = new Peer();
let ID ;
peer.on('open', function(id) {
	console.log('My peer ID is: ' + id);
  ID = id;
});

const inp = document.querySelector("input[data-peerid]");
const gobtn = document.querySelector("[data-go]");
const ishost = document.querySelector("input[type=checkbox]");
const sendbtn = document.querySelector("[data-send]")

gobtn.addEventListener("click", () => {
  const conn = peer.connect(inp.value);
  conn.on('open', function() {
    console.log("connected");
    sendbtn.addEventListener("click", () => {
      console.log("sent");
      conn.send("host " + ID)
    });
    // Receive messages
    conn.on('data', function(data) {
      console.log('host Received', data);
    });
  });
});


peer.on('connection', function(conn) {
  conn.on('open', function() {
    console.log("recieved connected");
    sendbtn.addEventListener("click", () => {
      conn.send("123123 " + ID)
    });
  });
  // Receive messages
  conn.on('data', function(data) {
    console.log('1223 Received', data);
  });
});

