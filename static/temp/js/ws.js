var ws;

function init() {

    ws = new WebSocket("ws://localhost:8080/dictionary");
    ws.send("loadWords");

    ws.onopen = function (event) {

    }
    ws.onmessage = function (event) {
    	alert(event.data);
    }
    ws.onclose = function (event) {
    }
};




