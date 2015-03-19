// getUserMedia cross-browser normalization
navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia
        || navigator.mozGetUserMedia || navigator.msGetUserMedia;
var audioContext = window.AudioContext || window.webkitAudioContext;

module.exports = function(onaudioprocess, onrecord) {
	if (navigator.getUserMedia) {
		var errorCallback = function(e) {
			console.log('ERROR: Rejected access to user media', e);
		}

		// Not showing vendor prefixes.
		navigator.getUserMedia({
		    video : false,
		    audio : true
		}, function(localMediaStream) {
			var context = new audioContext();
			var audioInput = context.createMediaStreamSource(localMediaStream);

			// create a javascript node
			var buffer = 2048;
			var recorder = context.createScriptProcessor(buffer, 2, 2);
			// specify the processing function
			if (onaudioprocess) {
				recorder.onaudioprocess = function(e) {
					// var left = e.inputBuffer.getChannelData(0);
					if (onaudioprocess(e) === false) {
						console.log('Stopping recording')
						recorder.onaudioprocess = undefined;
						audioInput.disconnect();
						recorder.disconnect();
						localMediaStream.stop();
					}
				};
			}

			// connect stream to our recorder
			audioInput.connect(recorder);
			// connect our recorder to the previous destination
			recorder.connect(context.destination);

			if (onrecord) {
				onrecord(localMediaStream);
			}

		}, errorCallback);

	} else {
		console.log('ERROR: getUserMedia() is not supported in your browser');
	}
}
