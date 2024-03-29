function initResolutionsByCellFunctions () {


	initSaveResolutionsByCellFunction();
	initChangeSolutionByCellFunction();
	

	
	
}







function initSaveResolutionsByCellFunction () {
	
	$(".save-resolutions-by-cell").click(function() {
		
		// Get data from uploads form.
		var data = $.toJSON($('.resolutions-by-cell-form').serializeObject());
		
		//alert(data);
		

		$.ajax({
			type: 'PUT',
			data: data,
			url: '/dataMerger/data/merges/' + urlParams["merge_id"] + '/resolutions-by-cell',
			dataType: 'json',
			success: function (data, textStatus, jqXHR) {

				//Just reload
				document.location.reload();
				
				//retrieveResolutionsByCellAsXHTMLUsingMergeId(urlParams["merge_id"]);
					

			},
			error: function (jqXHR, textStatus, errorThrown){
	            $('.error').html(errorThrown);
	            $('.status').html(textStatus);
	        },
			beforeSend: function() { $('.saving-indicator').show(); },
	        complete: function() { $('.saving-indicator').hide(); }
		});
		
	});
	
}

function initChangeSolutionByCellFunction () {
	

	$('.resolutions-by-cell-form select[name|=solution_by_cell_id]').change(function() {

		var row = $(this).closest("tr");
		
		//alert($(this).val());
		
		var selectName = $(this).attr('name');
		
		//19 is the lenght of "solution_by_cell_id"
		var suffix = selectName.substring(19, selectName.length);
		
		if ($(this).val() == '4') {
			row.find('td label[for|="constant' + suffix + '"]').css('display', 'inline');
			row.find('td input[name|="constant' + suffix + '"]').css('display', 'inline');
		} else {
			row.find('td label[for|="constant' + suffix + '"]').css('display', 'none');
			row.find('td input[name|="constant' + suffix + '"]').css('display', 'none');
		}

	});	
	
}

function retrieveResolutionsByCellAsXHTMLUsingMergeId (mergeId) {
	
	
	$.ajax({
		data: '',
		dataType: 'html',
		success: function (data, textStatus, jqXHR) { 
			
				$('.resolutions-by-cell-form').html(data);
				initChangeSolutionByCellFunction(); // the replaced HTML needs to be re-bound.
		},
		type: 'GET',
		url: '/dataMerger/data/merges/' + mergeId + '/resolutions-by-cell'
	});	
}