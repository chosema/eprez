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
