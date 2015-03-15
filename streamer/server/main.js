var logger = require('./logger')

var express = require('express')
var http = require("http")
var binaryjs = require('binaryjs')

var app = express()
//app.use(express.static('WebContent'))
app.get('/', function(req, res) {
	logger.info('Request received with params: ' + req.params)
	res.send('WebContent/index.html')
})

/* HTTP server initialization (with expressjs 'app' request handler) */
var httpServer = http.createServer(app).listen(3000)
var host = httpServer.address().address
var port = httpServer.address().port
logger.info('Web app listening at http://' + host + ':' + port)

/* WebSocket server initialization */
var binaryServer = binaryjs.BinaryServer({
    server : httpServer,
    path : '/ws'
});
binaryServer.on('connection', function(client) {
	logger.info('Client connected: ' + client.id)

	client.on('error', function(error) {
		logger.info('Error received from client: ' + client.id)
	});
	client.on('close', function() {
		logger.info('Closing connection from client: ' + client.id)
	});

	client.on('stream', function(stream, meta) {
		// TODO client stream
	});

	client.send("Hello World", {
		event : 'test'
	});
});
