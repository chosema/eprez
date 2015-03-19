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
