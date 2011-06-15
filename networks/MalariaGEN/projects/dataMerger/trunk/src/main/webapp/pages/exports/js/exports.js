function initExportsFunctions () {

	initDeleteExportFunction();
}

function initDeleteExportFunction () {

	$(".deleteExportButton").click(function() {	
		
		
		if ($(this).closest('tr').find('input[name="export_id"]').val() != null) {
			
			
			
			if (confirm("Are you sure you want to permanently remove export " + $(this).closest('tr').find('input[name="export_id"]').val() + "?")) {
				
				
				
					
				
				var deletingIndicator = $(this).closest('tr').find('.deleting-indicator');
				
					$.ajax({
						type: 'DELETE',
						data: '',
						url: '/dataMerger/data/exports/' + $(this).closest('tr').find('input[name="export_id"]').val(),
						dataType: 'json',
						success: function (data, textStatus, jqXHR) {
							
							if (data.success) {
								
								if (data.success = "true") {

									retrieveExportsAsDecoratedHTMLTable();
									
									initExportsFunctions(); //rebind
									alert("Export has been removed.");
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
			
			alert("The export has not been specified.");
		}
		
	});
	
}


function retrieveExportsAsDecoratedHTMLTable () {
	
	$.ajax({
			data: '',
			dataType: 'html',
			success: function (data, textStatus, jqXHR) { 
				$('.exports').html(data); 
				//Need to re-bind the new HTML
				initExportsFunctions();
			},
			type: 'GET',
			url: '/dataMerger/data/exports'
		});
	
}