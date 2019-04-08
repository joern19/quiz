'use strict';
var app = require('http').createServer(handler);
var fs = require('fs');
var port = 80; 
var io = require('socket.io')(app);

app.listen(port);

var lockcontrol = false;

function handler(req, res) {
    if (req.url.startsWith("/board")) {
        switch (req.url.substring(6)) {
            case "/layouts/main.js":
                res.writeHead(200, { 'Content-Type': 'text/javascript' });
                fs.createReadStream("layouts/board/main.js").pipe(res);
                break;
            case "/layouts/board.css":
                res.writeHead(200, { 'Content-Type': 'text/css' });
                fs.createReadStream("layouts/board/board.css").pipe(res);
                break;
            default:
                res.writeHead(200, { 'Content-Type': 'text/html' });
                fs.createReadStream("layouts/board/index.html").pipe(res);
        }
    } else if (req.url.startsWith("/control")) {
        if (!lockcontrol) {
            switch (req.url.substring(8)) {
                case "/layouts/main.js":
                    res.writeHead(200, { 'Content-Type': 'text/javascript' });
                    fs.createReadStream("layouts/control/main.js").pipe(res);
                    break;
                default:
                    res.writeHead(200, { 'Content-Type': 'text/html' });
                    fs.createReadStream("layouts/control/index.html").pipe(res);
            }
        }
    } else {
        switch (req.url) {
            case "/layouts/main.js":
                res.writeHead(200, { 'Content-Type': 'text/javascript' });
                fs.createReadStream("layouts/user/main.js").pipe(res);
                break;
            case "/layouts/user.css":
                res.writeHead(200, { 'Content-Type': 'text/css' });
                fs.createReadStream("layouts/user/user.css").pipe(res);
                break;
            default:
                res.writeHead(200, { 'Content-Type': 'text/html' });
                fs.createReadStream("layouts/user/index.html").pipe(res);
        }
    }
};

function getNameBySocketAndRemove(arr, socket) {
    var result = null;
    for (var i = 0; i < arr.length; i++) {
        if (arr[i][0] == socket) {
            result = arr[i][1];
            arr.splice(i, 1);
            break;
        }
    }
    return result;
}

var questions = {};
function loadQuestions(socket) {
    fs.readFile('games.json', 'utf8', function (err, data) {
        if (err) throw err;
        questions = JSON.parse(data);
        socket.emit('games', Object.keys(questions));
        console.log("loaded and sended games.json");
    });
}

var students = [];
var boards = [];
var controller;

var currentQuestion;
var questionType;
var questionLength;
var answers;
var correctAnswer; 
var points;

io.sockets.on('connection', function (socket) {
    socket.on('boardlogin', function () {
        boards.push(socket);
        sendStudents(students);
        console.log("A board connected. (" + boards.length + ")");
    });
    socket.on('login', function (name) {
        for (var student of students) {
            if (student[1] == name) {
                socket.emit('kick');
                return;
            }
        }
        students.push([socket, name, 0]);
        console.log(name + " logged in. (" + students.length + ")");
        sendStudents(students);
        controller.emit('join', name);
    });
    socket.on('disconnect', function () {
        var i = getNameBySocketAndRemove(students, socket);
        if (i != null) {
            console.log(i + " disconnected. (" + students.length + ")");
            sendStudents(students);
        } else {
            var a = boards.indexOf(socket);
            if (a != -1) {
                boards.splice(a, 1);
                console.log("A board disconnected. (" + boards.length + ")");
            } else if (socket == controller) {
                controller = undefined;
                lockcontrol = false;
                console.log("controller disconnected");
            }
        }
    });
    socket.on('controllogin', function () {
        if (!lockcontrol) {
            controller = socket;
            lockcontrol = true;
            console.log("controller connected, load/reload questions");
            loadQuestions(socket);
        }
    });
    socket.on('lockin', function (answer) {
        students[getStudentIndexBySocket(socket)][3] = answer;
        for (var student of students) {
            if (student[3] == undefined) {
                return;
            }
        }
        for (var student of students) {
            if (student[3] == correctAnswer - 1) {
                student[0].emit('correct');
                student[2] += points;
                sendStudents(students);
            } else {
                student[0].emit('wrong');
            }
        }
    });
    socket.on('startQuiz', function (gameName) {
        loadAndSendQuestion(gameName);
    });
});

function getStudentIndexBySocket(socket) {
    var counter = 0;
    for (var student of students) {
        if (student[0] == socket) {
            return counter;
        }
        counter++;
    }
    return null;
}

function loadAndSendQuestion(gameName) {
    currentQuestion = questions[gameName].questions[0].name;
    questionType = questions[gameName].questions[0].type;
    questionLength = questions[gameName].questions[0]["question-count"];
    if (questionType == 0) {
        answers = [questions[gameName].questions[0].a1, questions[gameName].questions[0].a2, questions[gameName].questions[0].a3, questions[gameName].questions[0].a4];
    } else if (questionType == 1) {
        //todo
    }
    correctAnswer = questions[gameName].questions[0].correct;
    points = questions[gameName].questions[0].points;
    for (var student of students) {
        student[0].emit('openQuestion', [currentQuestion, questionType, questionLength, answers]);
    }
    for (var board of boards) {
        board.emit('openQuestion', currentQuestion);
    }
}

function sendStudents(students) {
    var arr = [];
    for (var student of students) {
        var newStudent = [];
        newStudent.push(student[1]);
        newStudent.push(student[2]);
        arr.push(newStudent);
    }
    if (boards.length != 0) {
        arr.sort(function (a, b) {
            return a[1] - b[1];
        });
        arr.reverse();
        for (var i = 0; i < boards.length; i++) {
            boards[i].emit('scoreboard', arr);
        }
    }
}