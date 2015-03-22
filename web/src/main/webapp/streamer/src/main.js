'use strict'

var mainModule = 'eprezApp'

var app = angular.module(mainModule, [
        'ngResource',
        require('./services/_index').name,
        require('./controllers/_index').name
]);

angular.element(document).ready(function() {
	angular.bootstrap(document, [
		mainModule
	])
});

var waitForFinalEvent = (function() {
	var timer = null;
	return function(callback, ms) {
		if (timer == null) {
			timer = setTimeout(function() {
				callback();
				timer = null;
			}, ms);
		}
	};
})();
var resizeImages = function() {
	//console.log('Resized to ' + window.innerWidth + "x" + window.innerHeight);
	jQuery('.carousel-inner .img-responsive').each(function() {
		var thisImage = jQuery(this);
		thisImage.css("max-height", window.innerHeight - jQuery('.main-header').height() + 'px');
	});
};

resizeImages();
jQuery(window).resize(function() {
	waitForFinalEvent(resizeImages, 200);
});
