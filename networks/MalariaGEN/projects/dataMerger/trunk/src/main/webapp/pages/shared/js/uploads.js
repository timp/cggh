function initUploadsFunctions () {

	initCreateUploadUsingFileUploaderFunction();
}

//TODO: Rename this retrieveUploadsAsHTML()
function getUploads () {
	
	$.ajax({
			data: '',
			dataType: 'html',
			success: function (data, textStatus, jqXHR) { $('.uploads').html(data); },
			type: 'GET',
			url: '/dataMerger/data/uploads'
		});
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
            onComplete: function(id, fileName, responseJSON){ getUploads(); },
            onCancel: function(id, fileName){},
            
            messages: {
                // error messages, see qq.FileUploaderBasic for content            
            },
            showMessage: function(message){ alert(message); }    
        
        
    }); 	
	
}
