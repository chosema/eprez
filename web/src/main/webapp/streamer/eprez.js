(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\controllers\\_index.js":[function(require,module,exports){
module.exports = angular.module('eprezApp.controllers', [])
		.controller('rootController', require('./rootController'))
		.controller('recorderController', require('./recorderController'))
		.controller('playerController', require('./playerController'))
        .controller('documentCarouselController', require('./documentCarouselController'))

},{"./documentCarouselController":"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\controllers\\documentCarouselController.js","./playerController":"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\controllers\\playerController.js","./recorderController":"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\controllers\\recorderController.js","./rootController":"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\controllers\\rootController.js"}],"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\controllers\\documentCarouselController.js":[function(require,module,exports){
module.exports = [ '$scope', function($scope) {
    $scope.test = 'Hello World!'

    // var img = document.createElement("img")
    // img.src = (window.URL || window.webkitURL)
    // .createObjectURL(new Blob(parts))
    // document.body.appendChild(img)

}]

},{}],"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\controllers\\playerController.js":[function(require,module,exports){
module.exports = [ '$scope', function($scope) {

	var player = document.getElementById('player');
	var playerSource = document.getElementById('playerSource');

	player.playbackRate = 1.1;
	playerSource.src = _streamerHttpPath + '/play/' + $scope._token;
	playerSource.type = 'audio/mpeg';

	$scope._loaded.then(function() {
		if ($scope._userIsPresenter) { // user is presenter, nothing to do
		} else { // user is listener, play audio
			$scope.play();
		}
	});

	$scope.play = function() {
		$(playerSource).detach().appendTo(player);
		player.play();
	}
	$scope.pause = function() {
		player.pause();
	}
}];

},{}],"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\controllers\\recorderController.js":[function(require,module,exports){
module.exports = [ '$scope', 'webSocketService', function($scope, webSocketService) {
	$scope.stream = null;
	$scope.recording = false;
	$scope.encoder = null;
	$scope.input = null;
	$scope.node = null;
	$scope.samplerate = 22050;
	$scope.samplerates = [ 8000, 11025, 12000, 16000, 22050, 24000, 32000, 44100, 48000 ];
	$scope.bitrate = 64;
	$scope.bitrates = [ 8, 16, 24, 32, 40, 48, 56, 64, 80, 96, 112, 128, 144, 160, 192, 224, 256, 320 ];

	$scope.startRecording = function() {
		if ($scope.recording)
			return;
		console.log('INFO: start recording');
		$scope.encoder = new Worker('encoder.js');
		console.log('INFO: initializing encoder with samplerate=' + $scope.samplerate + ' and bitrate=' + $scope.bitrate);
		$scope.encoder.postMessage({ cmd: 'init', config: { samplerate: $scope.samplerate, bitrate: $scope.bitrate } });

		$scope.encoder.onmessage = function(e) {
			var sent = webSocketService.send('audio.stream.publish', e.data.buf);
			if (!sent && $scope.recording) {
				console.log('ERROR: Sending audio stream data to WebSocket failed');
				$scope.stopRecording(true);
			}
			if (e.data.cmd == 'end') {
				$scope.encoder.terminate();
				$scope.encoder = null;
			}
		}

		navigator.webkitGetUserMedia({ video: false, audio: true }, $scope.gotUserMedia, $scope.userMediaFailed);
	}

	$scope.userMediaFailed = function(code) {
		console.log('ERROR: grabbing microphone failed: ' + code);
	}

	$scope.gotUserMedia = function(localMediaStream) {
		$scope.recording = true;

		console.log('INFO: success grabbing microphone');
		$scope.stream = localMediaStream;

		var audio_context = new (window.AudioContext || window.webkitAudioContext)();

		$scope.input = audio_context.createMediaStreamSource($scope.stream);
		$scope.node = $scope.input.context.createScriptProcessor(4096, 1, 1);

		//console.log('sampleRate: ' + $scope.input.context.sampleRate);

		$scope.node.onaudioprocess = function(e) {
			if (!$scope.recording)
				return;
			var channelLeft = e.inputBuffer.getChannelData(0);
			$scope.encoder.postMessage({ cmd: 'encode', buf: channelLeft });
		};

		$scope.input.connect($scope.node);
		$scope.node.connect(audio_context.destination);

		$scope.$apply();
	}

	$scope.stopRecording = function(scopeApply) {
		if (!$scope.recording) {
			return;
		}
		console.log('INFO: stop recording');
		$scope.stream.stop();
		$scope.recording = false;
		$scope.encoder.postMessage({ cmd: 'finish' });

		$scope.input.disconnect();
		$scope.node.disconnect();
		$scope.input = $scope.node = null;

		if (scopeApply) {
			$scope.$apply();
		}
	}

}];

},{}],"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\controllers\\rootController.js":[function(require,module,exports){
module.exports = [ '$scope', '$q', 'dataService', function($scope, $q, dataService) {

	$scope._token = dataService.token;
	$scope._info = dataService.info;
	$scope._loaded = dataService.loaded;

	$scope._loaded.then(function() {
		$scope._presentation = $scope._info.presentation;
		$scope._user = $scope._info.user;
		$scope._userIsPresenter = $scope._info.userIsPresenter;
	});

}];

},{}],"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\main.js":[function(require,module,exports){
'use strict'

var mainModule = 'eprezApp'

var app = angular.module(mainModule, [
        'ngResource',
        require('./services/_index').name,
        require('./controllers/_index').name
])

angular.element(document).ready(function() {
	angular.bootstrap(document, [
		mainModule
	])
})

},{"./controllers/_index":"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\controllers\\_index.js","./services/_index":"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\services\\_index.js"}],"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\services\\_index.js":[function(require,module,exports){
module.exports = angular.module('eprezApp.services', [])
        .factory('dataService', require('./dataService'))
        .factory('webSocketService', require('./webSocketService'))

},{"./dataService":"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\services\\dataService.js","./webSocketService":"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\services\\webSocketService.js"}],"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\services\\dataService.js":[function(require,module,exports){
module.exports = [ '$resource', '$location', '$q', function($resource, $location, $q) {
	
	var rootPath = _contextPath + '/rest';
	var dataResource = $resource(rootPath, {}, {
		info: { url: rootPath + '/info/:token.json', method: 'GET' }
	});

	var dataService = {};
	dataService.token = $location.absUrl().split('\?')[1];
	dataService.info = dataResource.info({ token: dataService.token });
	dataService.loaded = $q.all([dataService.info.$promise]);

	return dataService;
}]

},{}],"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\services\\webSocketService.js":[function(require,module,exports){
module.exports = [ '$q', 'dataService', function($q, dataService) {

	var token = dataService.token;

	var ws;

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
					ws.send(joinUint8Arrays(strToUint8Array(message), new Uint8Array([0]), data));
				}
				return true;
			}
			return false;
		}
	}
}];

},{}]},{},["C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\main.js"]);
