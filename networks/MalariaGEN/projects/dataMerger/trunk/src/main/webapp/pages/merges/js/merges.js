function initMergesFunctions () {


	
	initExportFunction();
	initDeleteMergeFunction();
	
	
}

function initExportFunction () {
	
	$(".export-button").click(function() {
		
		//alert($(this).closest('tr').find('input[name="merge_id"]').val());
		
		if ($(this).closest('tr').find('input[name="merge_id"]').val() != null) {
		
			var dataObj = {mergedFileFilename : $(this).closest('tr').find("input[name=mergedFileFilename]").val()};
			
			var exportingIndicator = $(this).closest('tr').find('.exporting-indicator');
			
			$.ajax({
				type: 'POST',
				contentType: 'application/json',
				data: $.toJSON(dataObj),
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

function initDeleteMergeFunction () {

	$(".deleteMergeButton").click(function() {	
		
		
		if ($(this).closest('tr').find('input[name="merge_id"]').val() != null) {
		
				
			if (confirm("Are you sure you want to permanently remove merge " + $(this).closest('tr').find('input[name="merge_id"]').val() + "?\nWARNING: This will also delete all the joins and resolutions associated with this merge.")) {
	
					
				var deletingIndicator = $(this).closest('tr').find('.deleting-indicator');
				
					$.ajax({
						type: 'DELETE',
						data: '',
						url: '/dataMerger/data/merges/' + $(this).closest('tr').find('input[name="merge_id"]').val(),
						dataType: 'json',
						success: function (data, textStatus, jqXHR) {
							
							if (data.success) {
								
								if (data.success = "true") {

									retrieveMergesAsDecoratedHTMLTable();
									
									initMergesFunctions(); //rebind
									
									alert("Merge has been deleted.");
									
								} else {
									alert("An error occurred.");
								}
				
							} else {
								$('.status').html("textStatus: " + textStatus);
							}
						},
						error: function (jqXHR, textStatus, errorThrown){
				            $('.error').html("errorThrown: " + errorThrown);
				            $('.status').html("textStatus: " + textStatus);
				        },
						beforeSend: function() { deletingIndicator.show(); },
				        complete: function() { deletingIndicator.hide(); }
					});
					
	
			
			}
			
		} else {
			
			alert("The merge has not been specified.");
		}
		
	});
	
}


function retrieveMergesAsDecoratedHTMLTable () {
	
	$.ajax({
			data: '',
			dataType: 'html',
			success: function (data, textStatus, jqXHR) { 
				$('.merges').html(data); 
				//Need to re-bind the new HTML
				initMergesFunctions();
			},
			type: 'GET',
			url: '/dataMerger/data/merges'
		});
	
}