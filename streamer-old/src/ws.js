var binaryjs = require('binaryjs')

var logger = require('./logger')
var httpServer = require('./http')

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
        var sessionId = meta.id
        var token = meta.token

        logger.info('Received stream [id=' + sessionId + '] from client [' + client.id + ']');

        binaryServer.clients // all connected clients

        var pushAudioStream = client.createStream({ name: 'pushAudio' });
        stream.pipe(pushAudioStream);
        stream.on('data', function(data) {
            logger.info('Received data: ' + data.length);
        });
        stream.on('end', function() {
            pushAudioStream.end();
        });
    });
});

module.exports = binaryServer
