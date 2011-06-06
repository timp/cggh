function initResolutionsByRowFunctions () {


	initSaveResolutionsByRowFunction();
	initChangeSolutionByRowFunction();
	

	
	
}







function initSaveResolutionsByRowFunction () {
	
	$(".save-resolutions-by-row").click(function() {
		
		// Get data from uploads form.
		var data = $.toJSON($('.resolutions-by-row-form').serializeObject());
		
		//alert(data);
		

		$.ajax({
			type: 'PUT',
			data: data,
			url: '/dataMerger/data/merges/' + urlParams["merge_id"] + '/resolutions-by-row',
			dataType: 'json',
			success: function (data, textStatus, jqXHR) {

				//Just reload
				document.location.reload();
				
				//retrieveResolutionsByRowAsXHTMLUsingMergeId(urlParams["merge_id"]);
					

			},
			error: function (jqXHR, textStatus, errorThrown){
	            $('.error').html("errorThrown: " + errorThrown);
	            $('.status').html("textStatus: " + textStatus);
	        },
			beforeSend: function() { $('.saving-indicator').show(); },
	        complete: function() { $('.saving-indicator').hide(); }
		});
		
	});
	
}

function initChangeSolutionByRowFunction () {
	

	$('.resolutions-by-row-form select[name=solution_by_row_id]').change(function() {

		var row = $(this).closest("tr");
		
		//alert($(this).val());
		
		if ($(this).val() == '4') {
			row.find('td label[for|="constant"]').css('display', 'inline');
			row.find('td input[name|="constant"]').css('display', 'inline');
		} else {
			row.find('td label[for|="constant"]').css('display', 'none');
			row.find('td input[name|="constant"]').css('display', 'none');
		}

	});	
	
}

function retrieveResolutionsByRowAsXHTMLUsingMergeId (mergeId) {
	
	
	$.ajax({
		data: '',
		dataType: 'html',
		success: function (data, textStatus, jqXHR) { 
			
				$('.resolutions-by-row-form').html(data);
				initChangeSolutionByRowFunction(); // the replaced HTML needs to be re-bound.
		},
		type: 'GET',
		url: '/dataMerger/data/merges/' + mergeId + '/resolutions-by-row'
	});	
}