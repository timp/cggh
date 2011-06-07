function initFilesFunctions () {

	initCreateUploadUsingFileUploaderFunction();
	initMergeFilesFunction();
	initHideFilesFunction();
	initShowFilesFunction();
	initRemoveFilesFunction();
	
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
            	
            	retrieveFilesAsHTML(); 
            	
            },
            onCancel: function(id, fileName){},
            
            messages: {
                // error messages, see qq.FileUploaderBasic for content            
            },
            showMessage: function(message){ alert(message); }    
        
        
    }); 	
	
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