function initResolutionsFunctions () {


	initEditJoinFunction();	
	initExportFunction();

}

function initEditJoinFunction () {
	
	$(".edit-join").click(function() {
		
		if (urlParams["merge_id"] != null) {
		
			//TODO: Refactor this URL
			window.location.href = '/dataMerger/pages/merges/joins?merge_id=' + urlParams["merge_id"];
			
		} else {
			
			alert("The merge has not been specified.");
		}
		
	});
	
}

function initExportFunction () {
	
	$(".export-button").click(function() {
		
		if (urlParams["merge_id"] != null) {
		
			//TODO: Refactor this URL
			//window.location.href = '/dataMerger/pages/merges/joins?merge_id=' + urlParams["merge_id"];
			
			//alert("Go Go Go!");
			
			$.ajax({
				type: 'POST',
				data: '',
				url: '/dataMerger/data/merges/' + urlParams["merge_id"] + '/exports',
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
				beforeSend: function() { $('.exporting-indicator').show(); },
		        complete: function() { $('.exporting-indicator').hide(); }
			});
			
		} else {
			
			alert("The merge has not been specified.");
		}
		
	});
	
}