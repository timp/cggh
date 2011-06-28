function initXMLURL_to_CSVFileFunctions () {

	initTransformXMLURL_to_CSVRowsFileFunction();
	initSeeResponseFunction();

}



function initTransformXMLURL_to_CSVRowsFileFunction () {

	$(".transformXMLURL-to-CSVRowsFileButton").click(function() {	
		
		// Get data from form.
		var data = $.toJSON($('.new-transformation-form').serializeObject());
		
		var obj = $.parseJSON(data);
		
		if (obj != undefined && obj.URL != undefined && obj.URL != "") {

			 window.open('/transData/functions/data/transformers/XMLURL-to-CSVRowsFile?URL=' + obj.URL);
		
		} else {
			alert("Please provide a URL.");
		}
		
	});
}


function initSeeResponseFunction () {

	$(".seeResponseButton").click(function() {	
		
		// Get data from form.
		var data = $.toJSON($('.new-transformation-form').serializeObject());
		
		var obj = $.parseJSON(data);
		
		if (obj != undefined && obj.URL != undefined && obj.URL != "") {

			$.ajax({
				  url: '/transData/functions/data/transformers/XMLURL-to-CSVRowsFile?URL=' + obj.URL,
				  type: "GET",
				  dataType: "text",
				  success: function(data, textStatus, jqXHR){
					  $('.response').html(data);
				  },
				  error: function(jqXHR, textStatus, errorThrown){
				    alert("Error getting data: " + errorThrown);
				  } 
				});
			
		} else {
			alert("Please provide a URL.");
		}
		
	});
}

