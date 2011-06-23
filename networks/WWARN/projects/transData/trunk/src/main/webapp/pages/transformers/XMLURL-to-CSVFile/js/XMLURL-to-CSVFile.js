function initXMLURL_to_CSVFileFunctions () {

	initTransformXMLURL_to_CSVFileFunction();

}



function initTransformXMLURL_to_CSVFileFunction () {

	$(".transformXMLURL-to-CSVFileButton").click(function() {	
		
		// Get data from form.
		var data = $.toJSON($('.new-transformation-form').serializeObject());
		
		var obj = $.parseJSON(data);
		
		if (obj != undefined && obj.url != undefined && obj.url != "") {

			window.location.href = '/transData/functions/data/transformers/xmlurl-to-csvfile?url=' + obj.url;
			
			/*
			$.ajax({
				type: 'GET',
				data: '',
				url: '/transData/functions/data/transformers/xmlurl-to-csvfile?url=' + obj.url,
				success: function (data, textStatus, jqXHR) {
					
					alert(textStatus);
				},
				error: function (jqXHR, textStatus, errorThrown){
		            $('.error').html(errorThrown);
		        },
				beforeSend: function() { $('.transforming-indicator').show(); },
		        complete: function() { $('.transforming-indicator').hide(); }
			});
			*/
		
		} else {
			alert("Please provide a URL.");
		}
		
	});
}