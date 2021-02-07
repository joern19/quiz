let socket = new WebSocket("wss://" + location.host + "/controller"); //just hard code a secure connection. We got Let's encrypt...

function login() {
  socket.emit('controllogin');
}

socket.on('games', function (games) {
  var gameStart = document.getElementById("gameStart");
  var select = document.getElementById("games");
  for (var game of games) {
    var option = document.createElement("option");
    option.text = game;
    select.add(option);
  }
  gameStart.style.display = 'block';
});

socket.on('students', function (students) {
  var ul = document.getElementById("list");
  ul.innerHTML = '';
  if (students.length != 0) {
    for (var student of students) {
      var li = document.createElement("li");
      li.appendChild(document.createTextNode(student[0]));
      var input = document.createElement("input");
      input.type = "number";
      input.min = "0";
      input.value = student[1];
      input.id = student[0] + "_input";
      li.appendChild(input);
      var button = document.createElement("input");
      button.type = "button";
      button.value = "OK!";
      button.id = student[0];
      button.onclick = modifyStudent;
      li.appendChild(button);
      li.setAttribute("id", student[0]); // added line
      ul.appendChild(li);
    }
  }
});

function startQuiz() {
  message = {
    "type": "null",
    "message": "doesNotMatter"
  }
  socket.send(JSON.stringify(message));
}

socket.on('everybodyfinished', function (currentRoundIndex) {
  var select = document.getElementById("games");
  if (confirm('Start the Next Round?')) {
    socket.emit('startQuiz', [select.options[select.selectedIndex].text, currentRoundIndex + 1]);
  }
});

function nextRound() {
  console.log("Auf in die Nächste Runde.");
  alert("Not supported yet.");
}