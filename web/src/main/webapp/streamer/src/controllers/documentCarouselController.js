module.exports = [ '$scope', 'webSocketService', function($scope, webSocketService) {

	jQuery(document).on('keydown', function(event) {
		// keyCode = 37 left arrow
		// keyCode = 39 right arrow
		if (event.keyCode == 37) {
			$scope.onPreviousSlide();
		} else if (event.keyCode == 39) {
			$scope.onNextSlide();
		}
	});

	var documentCarousel = jQuery('#document-carousel');
	documentCarousel.carousel({
		interval: false
	});

	webSocketService.on('push:document.stream.publish', function(message) {
		console.log('Received new document page index: ' + message);
		setSlide(parseInt(message));
	});

	$scope._loaded.then(function() {
		var currentIndex = $scope._presentation.session.currentPageIndex;
		var pages = $scope._presentation.document.pages;
		if (pages && pages[currentIndex]) {
			setPageData(pages[currentIndex], false);
			setPageData(pages[currentIndex], true);
		}
	});

	$scope.onPreviousSlide = function() {
		if ($scope._presentation.session.currentPageIndex > 0) {
			var index = $scope._presentation.session.currentPageIndex;
    		webSocketService.send('send:document.stream.publish', index - 1);
		}
    };
    $scope.onNextSlide = function() {
    	if ($scope._presentation.session.currentPageIndex < $scope._presentation.document.pages.length - 1) {
    		var index = $scope._presentation.session.currentPageIndex;
    		webSocketService.send('send:document.stream.publish', index + 1);
    	}
    };

    function setSlide(index) {
    	$scope._presentation.session.currentPageIndex = index;
		setPageData($scope._presentation.document.pages[index], true, function() {
			documentCarousel.carousel(index);
		});
    }

    function setPageData(page, apply, sucessCallback, errorCallback) {
    	if (page.style === undefined) {
    		page.src = _streamerHttpPath + "/data/" + page.dataRef;
    		page.style = {
    			'background-image': "url('" + page.src + "')",
    			'background-repeat': "no-repeat",
    			'background-position': "center top",
    			'background-size': "contain"
    		};
    		if (apply) {
    			$scope.$apply();
    		}
    		jQuery("<img />").load(sucessCallback).error(errorCallback).attr("src", page.src);
    	} else if (sucessCallback) {
    		sucessCallback();
    	}
    }
}]
