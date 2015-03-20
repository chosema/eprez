module.exports = [ '$resource', '$location', '$q', function($resource, $location, $q) {
	
	var contextPath = '/web/streamer';
	var dataResource = $resource("/");
	
	var dataService = {
		path: $location.path(),
		token : $location.path().substring(1, $location.path().length),
		
		info: function() {
			dataService.token;
		}
	}

	return dataService;
}]
