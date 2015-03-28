module.exports = [ '$scope', '$q', 'dataService', function($scope, $q, dataService) {

	$scope._token = dataService.token;
	$scope._info = dataService.info;
	$scope._loaded = dataService.loaded;

	$scope._loaded.then(function() {
		$scope._presentation = $scope._info.presentation;
		$scope._user = $scope._info.user;
		$scope._userIsPresenter = $scope._info.userIsPresenter;

		setDisplayName($scope._user);
		setIdenticon($scope._user);

		for (var i = 0; i < $scope._presentation.session.tokens.length; i++) {
	        var userToken = $scope._presentation.session.tokens[i];
	        setDisplayName(userToken.user);
	        setIdenticon(userToken.user);
        }
	});

	function setDisplayName(user) {
		return user.displayName = (user.firstName && user.lastName ? (user.firstName + ' ' + user.lastName) : user.login);
	}
	function setIdenticon(user) {
		var prefix = 'data:image/png;base64,';
		return user.identicon = (prefix + new Identicon(user.id, 160).toString());
	}
}];
