let socket = new WebSocket("wss://" + location.host + "/monitor");

socket.onmessage = function (event) {
  let message = JSON.parse(event.data);
  switch (message.type) { //implement different message types
    case "alert":
      iziToast.info({ title: message.message });
      break;
    case "question":
      loadQuestion(message.message.question);
      break;
    default:
      console.log("Got message: " + event.data);
  }
}

function loadQuestion(question) {
  document.getElementById("question").innerHTML = question;
}