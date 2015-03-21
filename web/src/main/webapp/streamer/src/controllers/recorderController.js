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
			var sent = webSocketService.send('send:audio.stream.publish', e.data.buf);
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
