function initFilesFunctions () {

	initMergeFilesFunction();
	initHideFilesFunction();
	initShowFilesFunction();
	initRemoveFilesFunction();
	
}

function retrieveFilesAsHTML () {
	
	$.ajax({
			data: '',
			dataType: 'html',
			success: function (data, textStatus, jqXHR) { 
				$('.files').html(data); 
				//Need to re-bind the new HTML
				initFilesFunctions();
			},
			type: 'GET',
			url: '/dataMerger/data/files'
		});
	
}

function initMergeFilesFunction () {

	$(".merge-files-button").click(function() {	
		
		// Get data from files form.
		var data = $.toJSON($('.files-form').serializeObject());
		
		//Validate exactly two checkboxes selected.
		
		var obj = jQuery.parseJSON(data);
		
		if (obj != undefined && 
				
				(
				
					(obj.upload_id != undefined && obj.upload_id.length == 2)
					
					||
					
					(obj.upload_id != undefined && obj.upload_id.length == 1 && obj.export_id != undefined && obj.export_id.length == 1)
					
					||
					
					(obj.export_id != undefined && obj.export_id.length == 2)
					
				)
				
		) {
		
			$.ajax({
				type: 'POST',
				data: data,
				url: '/dataMerger/data/merges',
				dataType: 'json',
				success: function (data, textStatus, jqXHR) {
					
					if (data.id) {
						
						//TODO: This URL is ugly.
						//TODO: GET /dataMerger/pages/merges/[id]/join
						window.location.href = '/dataMerger/pages/merges/joins?merge_id=' + data.id;
		
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
				beforeSend: function() { $('.merging-indicator').show(); },
		        complete: function() { $('.merging-indicator').hide(); }
			});
		
		} else {
			alert("Please select two files to merge.");
		}
		
	});
}

function initHideFilesFunction () {

	$(".hide-files-button").click(function() {	
		
		
	});
	
}

function initShowFilesFunction () {

	$(".show-files-button").click(function() {	
		
		
	});
	
}

function initRemoveFilesFunction () {

	$(".remove-files-button").click(function() {	
		
		
	});
	
}