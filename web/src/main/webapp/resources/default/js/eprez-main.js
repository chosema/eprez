String.prototype.endsWith = function(suffix) {
    return this.indexOf(suffix, this.length - suffix.length) !== -1;
};

var Eprez = {
	getRemainingTime : function(remainingSeconds) {
		if (remainingSeconds > 0) {
			var remainingSecondsTmp = remainingSeconds;
			var days, hours, minutes;
			days = remainingSecondsTmp >= 86400 ? Math.floor(remainingSecondsTmp / 86400) : 0;
			remainingSecondsTmp -= days * 86400;
			hours = remainingSecondsTmp >= 3600 ? Math.floor(remainingSecondsTmp / 3600) : 0;
			remainingSecondsTmp -= hours * 3600;
			minutes = remainingSecondsTmp >= 60 ? Math.floor(remainingSecondsTmp / 60) : 0;
			remainingSecondsTmp -= minutes * 60;
			return [days, hours, minutes, remainingSecondsTmp];
		} else {
			return [];			
		}
	},
	formatRemainingTime : function(remainingTime) {
		function pad(a,b){return(1e15+a+"").slice(-b)};
		if (remainingTime.length == 0) {
			return "";
		} else {
			var days = remainingTime[0], hours = remainingTime[1], minutes = remainingTime[2], seconds = remainingTime[3];
			return (days == 0 ? "" : (days + (days == 1 ? " day " : " days ")))
			+ pad(hours, 2) + ":" + pad(minutes, 2) + ":" + pad(seconds, 2);
		}
	},
	formatRemainingSeconds : function(remainingSeconds) {
		return this.formatRemainingTime(this.getRemainingTime(remainingSeconds));
	}
};

jQuery(document).ready(function($) {

	$('.ui-autocomplete-multiple-container').on('click', function(event) {
		var id = jQuery(this).parent().attr('id');
		if (event.target.id.endsWith(id + '_input')) {
			var idSplit = id.split(':');
			var idLastFragment = idSplit[idSplit.length - 1];
			var autocompleteWV = PF(idLastFragment + 'WV');
			if (autocompleteWV) {
				autocompleteWV.search('');			
			} else {
				console.log('Cannot find autocomplete with WV=' + idLastFragment + 'WV' + ' resolved from id=' + id);
			}
		}
	});
	
	$('.ui-inplace-content input').on('keydown', function(event) {
		/*console.log('Keypress on input:');
		console.log(this);
		if (event.which == 13) {
			console.log('Enter pressed...clicking on:');
			console.log($(this).parents('.ui-inplace-content').find('.ui-inplace-save'));
			$(this).parents('.ui-inplace-content').find('.ui-inplace-save').click();
		}*/
	});

});
