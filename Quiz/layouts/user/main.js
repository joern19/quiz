var socket = io.connect('http://localhost');

window.onbeforeunload = function () {
    return "Du wirst deine Punkte verlieren! Trotzdem verlassen?";
};

function login() {
    var name = prompt("Bitte geben sie ihren Namen möglichst ohne Leerzeichen an.");
    if (name != undefined && name.replace(/\s/g, '') != "") {
        name = name.replace(/\s/g, '_');
        socket.emit('login', name);
        console.log(name);
        document.getElementById("question").textContent = name;
    } else {
        login();
    }
}

function sleep(time) {
    return new Promise((resolve) => setTimeout(resolve, time));
}

socket.on('openQuestion', function (question) {
    switch (question[1]) {
        case 0:
            fourAnswers(question[3]);
            document.getElementById("question").textContent = question[0];
    }
});

socket.on('wrong', function () {
    hide();
    console.log("wrong");
    document.getElementById("wrong").style.display = 'block';
});

socket.on('correct', function () {
    hide();
    console.log("correct");
    document.getElementById("correct").style.display = 'block';
});

socket.on('kick', function () {
    window.location = window.location.href;
});

function answer(buttonID) {
    sleep(200).then(() => {
        hide();
        document.getElementById("waiting").style.display = 'block';
        socket.emit('lockin', buttonID);
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
    document.getElementById("b0").textContent = a[0];
    document.getElementById("b1").textContent = a[1];
    document.getElementById("b2").textContent = a[2];
    document.getElementById("b3").textContent = a[3];
    document.getElementById("4answers").style.display = 'block';
}