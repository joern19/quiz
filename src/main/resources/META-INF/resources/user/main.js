var connected = false;
let socket = new WebSocket("wss://" + location.host + "/game");

var answerStrings = [];

window.onbeforeunload = function () {
  return "Du wirst deine Punkte verlieren! Trotzdem verlassen?";
};

function sleep(time) {
  return new Promise((resolve) => setTimeout(resolve, time));
}

socket.onmessage = function (event) {
  let message = JSON.parse(event.data);
  switch (message.type) { //implement different message types
    case "alert":
      iziToast.info({ title: message.message });
      break;
    case "question":
      loadQuestion(message.message);
      break;
    default:
      console.log("Unsupported Message type detected: " + message.type);
  }
}

function loadQuestion(question) {
  document.getElementById("question").innerHTML = question.question;
  fourAnswers(question.answersInRandomOrder);
}

function answer(buttonID) {
  sleep(200).then(() => {
    hide();
    document.getElementById("waiting").style.display = 'block';
    let message = { //TODO: test what happens if we send garbage. do we need to add validator?
      "type": "answer",
      "message": answerStrings[buttonID]
    }
    socket.send(JSON.stringify(message));
  });
}

function hide() {
  document.getElementById("waiting").style.display = 'none';
  document.getElementById("4answers").style.display = 'none';
  document.getElementById("textIn").style.display = 'none';
  document.getElementById("correct").style.display = 'none';
  document.getElementById("wrong").style.display = 'none';
}

function fourAnswers(a) {
  hide();
  answerStrings = a;
  document.getElementById("b0").textContent = a[0];
  document.getElementById("b1").textContent = a[1];
  document.getElementById("b2").textContent = a[2];
  document.getElementById("b3").textContent = a[3];
  document.getElementById("4answers").style.display = 'block';
}

