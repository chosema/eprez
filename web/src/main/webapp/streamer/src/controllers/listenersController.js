module.exports = [ '$scope', '$q', 'webSocketService', function($scope, $q, webSocketService) {

	webSocketService.on('push:presentation.listeners', function(message) {
		console.log('Received active listeners change: ' + message);
		var userTokens = $scope._presentation.session.tokens;
		if (userTokens) {
			for (var i = 0; i < userTokens.length; i++) {
	            var token = userTokens[i];
	            token.online = message.indexOf(token.id) != -1;
            }
		}
		$scope.$apply();
	});

}];
