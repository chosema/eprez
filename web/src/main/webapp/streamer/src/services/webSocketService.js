module.exports = [ '$q', 'dataService', function($q, dataService) {

	var token = dataService.token;

	var ws;
	
	var messageHandlers = {}

	if (token) {
		dataService.loaded.then(function() {
			console.log('INFO: Opening WebSocket connection as ' + (dataService.info.userIsPresenter ? 'presenter' : 'listener'));
			var address = _streamerWsPath + (dataService.info.userIsPresenter ? '/record/' : '/listen/') + token;
			
			ws = new WebSocket(address);
			ws.onopen = function() {
				console.log('INFO: WebSocket connection sucessfully opened on: ' + address);
			}
			ws.onerror = function() {
				ws = null;
			}
			ws.onclose = function() {
				console.log('WARN: WebSocket connection closed');
				ws = null;
			}
			ws.onmessage = function(message) {
				var data = JSON.parse(message.data);
				var typeHandlers = messageHandlers[data.type];
				if (typeHandlers) {
					for (var i = 0; i < typeHandlers.length; i++) {
						typeHandlers[i](data.content, message);
                    }
				}
			}
		});
	} else {
		ws = null;
		console.log('ERROR: WebSocket connection can not be established because of invalid token: ' + token);
	}

	function strToUint8Array(str) {
		if (str) {
			var uint = new Uint8Array(str.length);
			for (var i = 0, j = str.length; i < j; ++i) {
				uint[i] = str.charCodeAt(i);
			}
			return uint;
		} else {
			return new Uint8Array();
		}
	}
	
	function joinUint8Arrays(args) {
		var resultLength = 0;
		for (var i = 0; i < arguments.length; i++) {
			resultLength += arguments[i].length;
        }
		var joined = new Uint8Array(resultLength);
		var index = 0;
		for (var i = 0; i < arguments.length; i++) {
			var array = arguments[i];
			for (var j = 0; j < array.length; j++) {
				joined[index++] = array[j];
            }
        }
		return joined;
	}

	return {
		send: function(message, data) {
			if (ws) {
				if (data === undefined) {
					ws.send(joinUint8Arrays(strToUint8Array(message), new Uint8Array([0])));
				} else {
					if (data instanceof Uint8Array) {
						ws.send(joinUint8Arrays(strToUint8Array(message), new Uint8Array([0]), data));
					} else {
						ws.send(joinUint8Arrays(strToUint8Array(message), new Uint8Array([0]), strToUint8Array(data.toString())));
					}
				}
				return true;
			}
			return false;
		},
		on: function(type, callback) {
			var typeHandlers = messageHandlers[type];
			if (typeHandlers === undefined) {
				messageHandlers[type] = typeHandlers = [];
			}
			typeHandlers.push(callback);
		}
	}
}];
