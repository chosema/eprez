module.exports = [ '$q', 'dataService', function($q, dataService) {

	var token = dataService.token;

	var ws;

	if (token) {
		var address = 'ws://localhost:8090/record/' + token;
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
					ws.send(joinUint8Arrays(strToUint8Array(message), new Uint8Array([0]), data));
				}
				return true;
			}
			return false;
		}
	}
}];
