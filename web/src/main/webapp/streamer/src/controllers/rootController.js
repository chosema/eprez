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
