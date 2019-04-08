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

socket.on('');

function startQuiz() {
    var gameStart = document.getElementById("gameStart");
    var select = document.getElementById("games");
    socket.emit('startQuiz', select.options[select.selectedIndex].text);
    gameStart.style.display = 'none';
}

function nextRound() {
    console.log("Auf in die Nächste Runde.");
}
