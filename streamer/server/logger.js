var winston = require('winston')
var fs = require('fs')

var serverLogDirectory = '.logs'
var serverLogFileName = 'server.log'
var serverLogPath = serverLogDirectory + '/' + serverLogFileName

fs.exists(serverLogPath, function(exists) {
	if (!exists) {
		console.log('Creating directory for log files');
		fs.mkdir(serverLogDirectory)
	}
});

var logger = new (winston.Logger)({
	transports : [ new (winston.transports.Console)(),
			new (winston.transports.File)({
				filename : serverLogPath
			}) ]
})

module.exports = logger
