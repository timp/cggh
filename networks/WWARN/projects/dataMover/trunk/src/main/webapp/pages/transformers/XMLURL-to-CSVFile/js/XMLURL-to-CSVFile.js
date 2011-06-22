function initXMLURL_to_CSVFileFunctions () {

	initTransformXMLURL_to_CSVFileButtonFunction();

}



function initTransformXMLURL_to_CSVFileButtonFunction () {

	$(".transformXMLURL-to-CSVFileButton").click(function() {	
		
		// Get data from form.
		var data = $.toJSON($('.url-form').serializeObject());
		
		//Validate exactly two checkboxes selected.
		
		var obj = jQuery.parseJSON(data);
		
		if (obj != undefined && obj.url != undefined && obj.url != "") {
		
			$.ajax({
				type: 'GET',
				data: data,
				url: '/dataMerger/functions/data/transformers/xmlurl-to-csvfile',
				dataType: 'json',
				success: function (data, textStatus, jqXHR) {
					
					if (data.id) {
						
						alert("OK");
		
					} else {
						alert("An error occurred.");
					}
				},
				error: function (jqXHR, textStatus, errorThrown){
		            $('.error').html(errorThrown);
		            $('.status').html(textStatus);
		        },
				beforeSend: function() { $('.transforming-indicator').show(); },
		        complete: function() { $('.transforming-indicator').hide(); }
			});
		
		} else {
			alert("Please provide a URL.");
		}
		
	});
}