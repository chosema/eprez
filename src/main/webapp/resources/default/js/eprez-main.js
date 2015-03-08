String.prototype.endsWith = function(suffix) {
    return this.indexOf(suffix, this.length - suffix.length) !== -1;
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
