'use strict'

var eventBus = {}
module.exports = function(address, sessionId) {
	//this.address = address
	//this.sessionId = sessionId
	this.register = function(event, ondata, onend) {
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
	};
	this.connect = function(onconnect) {
		var client = BinaryClient(address, { sessionId: sessionId })
		client.on('open', function(stream) {
			console.log('Client opened on: ' + address)

			if (onconnect) {
				onconnect();
			}

			// Received stream from server
			client.on('stream', function(stream, meta) {
				if (meta.name && eventBus[meta.name]) {
					var eventHandlers = eventBus[meta.name]

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
