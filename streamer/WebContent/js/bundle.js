(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez.streamer\\app-client\\client.js":[function(require,module,exports){
'use strict'

var eventBus = {}

var address = 'ws://localhost:3000/ws'

module.exports = {
	register : function(event, ondata, onend) {
		if (eventBus[event] === undefined) {
			var handlers = []
			eventBus[event] = {
				handlers: handlers,
				publish: function(type, arg0, arg1, arg2, arg3, arg4, arg5, arg6) {
					for (var i = 0; i < handlers.length; i++) {
	                    var handler = this.handlers[i];
	                    var handlerForType = handler[type];
	                    if (typeof(handlerForType) === 'function') {
	                    	handlerForType(arg0, arg1, arg2, arg3, arg4, arg5, arg6)
	                    }
                    }
				}
			}
		}
		eventBus[event].handlers.push({
		    ondata : ondata,
		    onend : onend
		});
	},
	connect : function() {
		var client = BinaryClient(address)
		client.on('open', function(stream) {
			console.log('Client opened on: ' + address)

			// Received stream from server
			client.on('stream', function(stream, meta) {
				if (meta.event && eventBus[meta.event]) {
					var eventHandlers = eventBus[meta.event]

					var parts = []
					stream.on('data', function(data) {
						parts.push(data)
						eventHandlers.publish('ondata', data, parts, stream, meta)
					})
					stream.on('end', function() {
						// here we already have some data from stream in variable 'parts'
						eventHandlers.publish('onend', parts, stream, meta)
					})
				} else {
					console.log('ERROR: Received stream does not have any handler:')
					console.log(meta)
				}
			})
		})
		return client
	}
}

},{}],"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez.streamer\\app-client\\main.js":[function(require,module,exports){
'use strict'

var client = require('./client')

var app = angular.module('eprezApp', [])
angular.element(document).ready(function() {
	angular.bootstrap(document, [
		'eprezApp'
	])
})

client.register('test', function(data, parts, stream, meta) {
	console.log('Received data from test event: ')
	console.log(data)
})
client.connect()

app.controller('mainController', [
        '$scope', function($scope) {
	        $scope.test = 'Hello World!'

	        // var img = document.createElement("img")
	        // img.src = (window.URL || window.webkitURL)
	        // .createObjectURL(new Blob(parts))
	        // document.body.appendChild(img)
        }
])

},{"./client":"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez.streamer\\app-client\\client.js"}]},{},["C:\\Users\\pchov_000\\WorkspaceSTS\\eprez.streamer\\app-client\\main.js"]);
