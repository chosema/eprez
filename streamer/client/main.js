'use strict'

var client = require('./client')

var app = angular.module('eprezApp', [])
angular.element(document).ready(function() {
	angular.bootstrap(document, [
		'eprezApp'
	])
})

client.register('test', function(data, parts, stream, meta) {
	console.log('Received data from test event: ')
	console.log(data)
})
client.connect()

app.controller('mainController', [
        '$scope', function($scope) {
	        $scope.test = 'Hello World!'

	        // var img = document.createElement("img")
	        // img.src = (window.URL || window.webkitURL)
	        // .createObjectURL(new Blob(parts))
	        // document.body.appendChild(img)
        }
])
