function initFilesFunctions () {

	initCreateUploadUsingFileUploaderFunction();
	initMergeFilesFunction();
	initHideFilesFunction();
	initShowHiddenFilesFunction();
	initRemoveFilesFunction();
	initUnhideFilesFunction();
	initShowUnhiddenFilesFunction();
	initSortByFilenameFunction();
}


function initCreateUploadUsingFileUploaderFunction () {
	

	var uploader = new qq.FileUploader({

		// pass the dom node (ex. $(selector)[0] for jQuery users)
        element: document.getElementById('file-uploader'),
        
            // url of the server-side upload script, should be on the same domain
            action: '/dataMerger/files/uploads',
            // additional data to send, name-value pairs
            params: {},
            
            // validation    
            // ex. ['jpg', 'jpeg', 'png', 'gif'] or []
            allowedExtensions: [],        
            // each file size limit in bytes
            // this option isn't supported in all browsers
            sizeLimit: 0, // max size   
            minSizeLimit: 0, // min size
            
            // set to true to output server response to console
            debug: true,
            
            // events         
            // you can return false to abort submit
            onSubmit: function(id, fileName){},
            onProgress: function(id, fileName, loaded, total){},
            onComplete: function(id, fileName, responseJSON){ 
            	
            	//Note: Just reloading would get around the FF3 back-button async amnesia and not much difference in performance
            	// but the async error would then be erased.
            	// document.location.reload();
            	
            	retrieveFilesAsDecoratedHTMLTable(); 
            	
            },
            onCancel: function(id, fileName){},
            
            messages: {
                // error messages, see qq.FileUploaderBasic for content            
            },
            showMessage: function(message){ alert(message); }    
        
        
    }); 	
	
}

function retrieveFilesAsDecoratedHTMLTable () {
	
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

function retrieveHiddenFilesAsDecoratedHTMLTable () {
	
	$.ajax({
			data: '',
			dataType: 'html',
			success: function (data, textStatus, jqXHR) { 
				$('.files').html(data); 
				//Need to re-bind the new HTML
				initFilesFunctions();
			},
			type: 'GET',
			url: '/dataMerger/data/files?hidden'
		});
	
}

function initMergeFilesFunction () {

	$(".merge-files-button").click(function() {	
		
		// Get data from files form.
		var data = $.toJSON($('.files-form').serializeObject());
		
		//Validate exactly two checkboxes selected.
		
		var obj = jQuery.parseJSON(data);
		
		if (obj != undefined && obj.file_id != undefined && obj.file_id.length == 2) {
		
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
		
		// Get data from files form.
		var data = $.toJSON($('.files-form').serializeObject());
		
		//Validate exactly two checkboxes selected.
		
		var obj = jQuery.parseJSON(data);
		
		if (obj != undefined && obj.file_id != undefined && obj.file_id.length > 0) {
		
			$.ajax({
				type: 'PUT',
				data: data,
				url: '/dataMerger/data/files?hide',
				dataType: 'json',
				success: function (data, textStatus, jqXHR) {
					
					if (data.success) {
						
						if (data.success = "true") {
							retrieveFilesAsDecoratedHTMLTable();
							initFilesFunctions(); //rebind
							alert("Files have been hidden.");
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
				beforeSend: function() { $('.hiding-indicator').show(); },
		        complete: function() { $('.hiding-indicator').hide(); }
			});
		
		} else {
			alert("Please select files to hide.");
		}		
		
	});
	
}

function initUnhideFilesFunction () {

	$(".unhide-files-button").click(function() {	
		
		// Get data from files form.
		var data = $.toJSON($('.files-form').serializeObject());
		
		//Validate exactly two checkboxes selected.
		
		var obj = jQuery.parseJSON(data);
		
		if (obj != undefined && obj.file_id != undefined && obj.file_id.length > 0) {
		
			$.ajax({
				type: 'PUT',
				data: data,
				url: '/dataMerger/data/files?unhide',
				dataType: 'json',
				success: function (data, textStatus, jqXHR) {
					
					if (data.success) {
						
						if (data.success = "true") {
							retrieveHiddenFilesAsDecoratedHTMLTable();
							initFilesFunctions(); //rebind
							alert("Files have been unhidden.");
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
				beforeSend: function() { $('.hiding-indicator').show(); },
		        complete: function() { $('.hiding-indicator').hide(); }
			});
		
		} else {
			alert("Please select files to hide.");
		}		
		
	});
	
}

function initShowHiddenFilesFunction () {

	$(".show-hidden-files-button").click(function() {	
		
		retrieveHiddenFilesAsDecoratedHTMLTable();
		
	});
	
}

function initShowUnhiddenFilesFunction () {

	$(".show-unhidden-files-button").click(function() {	
		
		retrieveFilesAsDecoratedHTMLTable();
		
	});
	
}

function initSortByFilenameFunction () {

	$(".sortByFilenameLink").click(function() {	
		
		retrieveFilesSortedByFilenameAsDecoratedHTMLTable();
		
	});
	
}

function initRemoveFilesFunction () {

	$(".remove-files-button").click(function() {	
		
		
		if (confirm("Are you sure you want to permanently remove these files?")) {
		
			// Get data from files form.
			var data = $.toJSON($('.files-form').serializeObject());
			
			//Validate exactly two checkboxes selected.
			
			var obj = jQuery.parseJSON(data);
			
			if (obj != undefined && obj.file_id != undefined && obj.file_id.length > 0) {
			
				$.ajax({
					type: 'DELETE',
					data: data,
					url: '/dataMerger/data/files',
					dataType: 'json',
					success: function (data, textStatus, jqXHR) {
						
						if (data.success) {
							
							if (data.success == "true") {
								
								//FIXME: Could be viewing either the hidden or unhidden list.
								// This loads the unhidden list regardless.
								retrieveFilesAsDecoratedHTMLTable();
								
								initFilesFunctions(); //rebind
								alert("Files have been deleted.");
							} else {
								
								//Reload anyway, because some files may have been deleted successfully.
								//FIXME: Could be viewing either the hidden or unhidden list.
								// This loads the unhidden list regardless.
								retrieveFilesAsDecoratedHTMLTable();
								
								initFilesFunctions(); //rebind
								
								alert("Cannot delete files that are being referred to by merges or exports.");
							}
			
						} else {
							$('.status').html("textStatus: " + textStatus);
						}
					},
					error: function (jqXHR, textStatus, errorThrown){
			            $('.error').html("errorThrown: " + errorThrown);
			            $('.status').html("textStatus: " + textStatus);
			        },
					beforeSend: function() { $('.removing-indicator').show(); },
			        complete: function() { $('.removing-indicator').hide(); }
				});
			
			} else {
				alert("Please select files to remove.");
			}
		
		}
		
	});
	
}

function retrieveFilesSortedByFilenameAsDecoratedHTMLTable () {
	
	$.ajax({
			data: '',
			dataType: 'html',
			success: function (data, textStatus, jqXHR) { 
				$('.files').html(data); 
				//Need to re-bind the new HTML
				initFilesFunctions();
			},
			type: 'GET',
			url: '/dataMerger/data/files?sort=filename'
		});
	
}