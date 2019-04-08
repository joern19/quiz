var socket = io.connect('http://localhost');

function setScoreboard(list) {
    var sb = document.getElementById("scoreboard");
    for (var i = sb.rows.length - 1; i > 0; i--) {
        sb.deleteRow(i);
    }
    for (var i = 0; i < list.length; i++) {
        var row = sb.insertRow(i + 1);
        var cell1 = row.insertCell(0);
        var cell2 = row.insertCell(1);
        var cell3 = row.insertCell(2);
        cell1.innerHTML = list[i][0];
        cell2.innerHTML = list[i][1];
        cell3.innerHTML = "Platz " + (i + 1) + ".";
    }
}

function login() {
    socket.emit('boardlogin');
}

socket.on('scoreboard', function (data) {
    console.log(data);
    setScoreboard(data);
});

socket.on('openQuestion', function (question) {
    document.getElementById("question").textContent = question;
});

//var arr = [["Jens Hirschfeld", 47],["Jörn Hirschfeld", 14],["Sören Hirschfeld", 10],["Max Mustermann", 3]];

