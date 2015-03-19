(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\controllers\\_index.js":[function(require,module,exports){
module.exports = angular.module('eprezApp.controllers', [])
        .controller('documentCarouselController', require('./documentCarouselController'))
        .controller('audioController', require('./audioController'))
        .controller('recorderController', require('./recorderController'))

},{"./audioController":"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\controllers\\audioController.js","./documentCarouselController":"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\controllers\\documentCarouselController.js","./recorderController":"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\controllers\\recorderController.js"}],"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\controllers\\audioController.js":[function(require,module,exports){
module.exports = [ '$scope', 'clientService', function($scope, clientService) {

    // get from clientService
    var sessionId = 'TODO'
    var presenter = false

}]

},{}],"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\controllers\\documentCarouselController.js":[function(require,module,exports){
module.exports = [ '$scope', function($scope) {
    $scope.test = 'Hello World!'

    // var img = document.createElement("img")
    // img.src = (window.URL || window.webkitURL)
    // .createObjectURL(new Blob(parts))
    // document.body.appendChild(img)

}]

},{}],"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\controllers\\recorderController.js":[function(require,module,exports){
module.exports = [ '$scope', function($scope) {
	$scope.stream = null;
	$scope.recording = false;
	$scope.encoder = null;
	$scope.ws = null;
	$scope.input = null;
	$scope.node = null;
	$scope.samplerate = 22050;
	$scope.samplerates = [ 8000, 11025, 12000, 16000, 22050, 24000, 32000, 44100, 48000 ];
	$scope.bitrate = 64;
	$scope.bitrates = [ 8, 16, 24, 32, 40, 48, 56, 64, 80, 96, 112, 128, 144, 160, 192, 224, 256, 320 ];
	$scope.recordButtonStyle = "red-btn";

	$scope.startRecording = function() {
		if ($scope.recording)
			return;
		console.log('start recording');
		$scope.encoder = new Worker('encoder.js');
		console.log('initializing encoder with samplerate = ' + $scope.samplerate + ' and bitrate = ' + $scope.bitrate);
		$scope.encoder.postMessage({ cmd: 'init', config: { samplerate: $scope.samplerate, bitrate: $scope.bitrate } });

		$scope.encoder.onmessage = function(e) {
			$scope.ws.send(e.data.buf);
			if (e.data.cmd == 'end') {
				$scope.ws.close();
				$scope.ws = null;
				$scope.encoder.terminate();
				$scope.encoder = null;
			}
		};

		$scope.ws = new WebSocket("ws://localhost:8090/record/54fb5905ddccdd16d857f5be");
		$scope.ws.onopen = function() {
			navigator.webkitGetUserMedia({ video: false, audio: true }, $scope.gotUserMedia, $scope.userMediaFailed);
		};
	};

	$scope.userMediaFailed = function(code) {
		console.log('grabbing microphone failed: ' + code);
	};

	$scope.gotUserMedia = function(localMediaStream) {
		$scope.recording = true;
		$scope.recordButtonStyle = '';

		console.log('success grabbing microphone');
		$scope.stream = localMediaStream;

		var audio_context = new window.webkitAudioContext();

		$scope.input = audio_context.createMediaStreamSource($scope.stream);
		$scope.node = $scope.input.context.createScriptProcessor(4096, 1, 1);

		console.log('sampleRate: ' + $scope.input.context.sampleRate);

		$scope.node.onaudioprocess = function(e) {
			if (!$scope.recording)
				return;
			var channelLeft = e.inputBuffer.getChannelData(0);
			$scope.encoder.postMessage({ cmd: 'encode', buf: channelLeft });
		};

		$scope.input.connect($scope.node);
		$scope.node.connect(audio_context.destination);

		$scope.$apply();
	};

	$scope.stopRecording = function() {
		if (!$scope.recording) {
			return;
		}
		$scope.recordButtonStyle = "red-btn";
		console.log('stop recording');
		$scope.stream.stop();
		$scope.recording = false;
		$scope.encoder.postMessage({ cmd: 'finish' });

		$scope.input.disconnect();
		$scope.node.disconnect();
		$scope.input = $scope.node = null;
	};

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
        .factory('clientService', require('./clientService'))
        .factory('dataService', require('./dataService'))

},{"./clientService":"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\services\\clientService.js","./dataService":"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\services\\dataService.js"}],"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\services\\clientService.js":[function(require,module,exports){
module.exports = [ '$http', '$q', function($http, $q) {

    return {

    }
}]

},{}],"C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\services\\dataService.js":[function(require,module,exports){
module.exports = [ '$resource', '$q', function($resource, $q) {

}]

},{}]},{},["C:\\Users\\pchov_000\\WorkspaceSTS\\eprez\\web\\src\\main\\webapp\\streamer\\src\\main.js"]);
