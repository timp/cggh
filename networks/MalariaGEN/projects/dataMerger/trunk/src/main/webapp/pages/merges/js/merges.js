function initMergesFunctions () {


	
	initExportFunction();
	
	
}

function initExportFunction () {
	
	$(".export-button").click(function() {
		
		//alert($(this).closest('tr').find('input[name="merge_id"]').val());
		
		if ($(this).closest('tr').find('input[name="merge_id"]').val() != null) {
		
			//TODO: Refactor this URL
			//window.location.href = '/dataMerger/pages/merges/joins?merge_id=' + urlParams["merge_id"];
			
			//alert("Go Go Go!");
			
			var exportingIndicator = $(this).closest('tr').find('.exporting-indicator');
			
			$.ajax({
				type: 'POST',
				data: '',
				url: '/dataMerger/data/merges/' + $(this).closest('tr').find('input[name="merge_id"]').val() + '/exports',
				dataType: 'json',
				success: function (data, textStatus, jqXHR) {
					
					if (data.id) {
						
						window.location.href = '/dataMerger/pages/exports';
		
					} else {
						alert("data: " + data);
						alert("data.id: " + data.id);
						$('.status').html("textStatus: " + textStatus);
					}
				},
				error: function (jqXHR, textStatus, errorThrown){
		            $('.error').html("errorThrown: " + errorThrown);
		            $('.status').html("textStatus: " + textStatus);
		        },
				beforeSend: function() { exportingIndicator.show(); },
		        complete: function() { exportingIndicator.hide(); }
			});
			
		} else {
			
			alert("The merge has not been specified.");
		}
		
	});
	
}