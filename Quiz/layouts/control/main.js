var socket = io.connect('http://localhost');

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

socket.on('join', function (name) {
    var ul = document.getElementById("list");
    var li = document.createElement("li");
    li.appendChild(document.createTextNode(name));
    var input = document.createElement("input");
    input.type = "number";
    input.min = "0";
    input.id = name + "_input";
    li.appendChild(input);
    var input = document.createElement("input");
    input.type = "button";
    input.value = "OK!";
    input.onclick = "modifyStudent(" + name + ")";
    li.appendChild(input);
    li.setAttribute("id", name); // added line
    ul.appendChild(li);
});

function modifyStudent(name) {
    alert(name);
}

function startQuiz() {
    var gameStart = document.getElementById("gameStart");
    var select = document.getElementById("games");
    socket.emit('startQuiz', select.options[select.selectedIndex].text);
    gameStart.style.display = 'none';
}

function nextRound() {
    console.log("Auf in die Nächste Runde.");
}
