'use strict';

var express = require('express');
var bodyParser = require('body-parser')

var app = express();

// parse application/x-www-form-urlencoded
app.use(bodyParser.urlencoded({ extended: false }))

// parse application/json
app.use(bodyParser.json())

// parse application/vnd.api+json as json
app.use(bodyParser.json({ type: 'application/vnd.api+json' }))

app.post('/compute', function (req, res) {
    console.log("receiving a compute request " + JSON.stringify(req.body));

    var result = Number.parseInt(req.body.a) + Number.parseInt(req.body.b);
    var data = {};
    data.sum = result;
    res.setHeader('Content-Type', 'application/json');
    res.send(JSON.stringify(data));
});

var server = app.listen(1890, function () {

  var host = server.address().address
  var port = server.address().port

  console.log("Sample REST Service listening at http://%s:%s", host, port)

})