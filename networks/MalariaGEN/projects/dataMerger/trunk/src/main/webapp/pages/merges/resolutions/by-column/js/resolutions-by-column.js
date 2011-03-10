function initResolutionsByColumnFunctions () {


	initEditJoinFunction();	
	
	initSaveResolutionsByColumnFunction();
	initChangeSolutionByColumnFunction();

	
}









function initSaveResolutionsByColumnFunction () {
	
	$(".save-resolutions-by-column").click(function() {
		
		// Get data from uploads form.
		var data = $.toJSON($('.resolutions-by-column-form').serializeObject());
		
		//alert(data);

		
		
		$.ajax({
			type: 'PUT',
			data: data,
			url: '/dataMerger/data/merges/' + urlParams["merge_id"] + '/resolutions-by-column',
			dataType: 'json',
			success: function (data, textStatus, jqXHR) {

				retrieveResolutionsByColumnAsXHTMLUsingMergeId(urlParams["merge_id"]);
					

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

function retrieveResolutionsByColumnAsXHTMLUsingMergeId (mergeId) {
	
	
	$.ajax({
		data: '',
		dataType: 'html',
		success: function (data, textStatus, jqXHR) { 
			
				$('.resolutions-by-column-form').html(data);
				initChangeSolutionByColumnFunction(); // the replaced HTML needs to be re-bound.
		},
		type: 'GET',
		url: '/dataMerger/data/merges/' + mergeId + '/resolutions-by-column'
	});	
}


function initChangeSolutionByColumnFunction () {
	

	$('.resolutions-by-column-form select[name=solution_by_column_id]').change(function() {

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

function initEditJoinFunction () {
	
	$(".edit-join").click(function() {
		
		if (urlParams["merge_id"] != null) {
		
			//TODO: Refactor this URL
			window.location.href = '/dataMerger/pages/merges/joins?merge_id=' + urlParams["merge_id"];
			
		} else {
			
			alert("The merge has not been specified ID.");
		}
		
	});
	
}
