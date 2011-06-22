function initXMLURL_to_CSVFileFunctions () {

	initTransformXMLURL_to_CSVFileFunction();

}



function initTransformXMLURL_to_CSVFileFunction () {

	$(".transformXMLURL-to-CSVFileButton").click(function() {	
		
		// Get data from form.
		var data = $.toJSON($('.new-transformation-form').serializeObject());
		
		//Validate exactly two checkboxes selected.
		
		var obj = $.parseJSON(data);
		
		if (obj != undefined && obj.url != undefined && obj.url != "") {
		
			$.ajax({
				type: 'GET',
				data: data,
				url: '/transData/functions/data/transformers/xmlurl-to-csvfile',
				dataType: 'json',
				success: function (data, textStatus, jqXHR) {
					alert(data);
						alert(textStatus);
				},
				error: function (jqXHR, textStatus, errorThrown){
		            $('.error').html(errorThrown);
		            alert(textStatus);
		        },
				beforeSend: function() { $('.transforming-indicator').show(); },
		        complete: function() { $('.transforming-indicator').hide(); }
			});
		
		} else {
			alert("Please provide a URL.");
		}
		
	});
}