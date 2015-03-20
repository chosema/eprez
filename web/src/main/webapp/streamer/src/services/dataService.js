module.exports = [ '$resource', '$location', '$q', function($resource, $location, $q) {
	
	return {
		path: $location.path(),
		token : $location.path().substring(1, $location.path().length)
	}
}]
