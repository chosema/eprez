module.exports = [ '$scope', '$q', function($scope, $q) {

	jQuery(document).ready(function() {
		setTimeout(function() {
			jQuery('#loadingIndicator').hide();
			jQuery('#contentWrapper').show();
		}, 2000);
	});
}];
