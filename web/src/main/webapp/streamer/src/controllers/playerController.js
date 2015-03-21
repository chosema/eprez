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
