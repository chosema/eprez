module.exports = angular.module('eprezApp.services', [])
        .factory('dataService', require('./dataService'))
        .factory('webSocketService', require('./webSocketService'))
