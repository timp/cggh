function initUploadsScripts () {

	//TODO: This will probably need to be shared.
	initSerializeObjectFunction();

	initMergeButton();
}

function getUploads () {
	
	$.ajax({
			data: '',
			dataType: 'html',
			success: function (data, textStatus, jqXHR) { $('.uploads').html(data); },
			type: 'GET',
			url: '/dataMerger/data/uploads'
		});
}

function initSerializeObjectFunction () {
	
	$.fn.serializeObject = function()
	{
	    var o = {};
	    var a = this.serializeArray();
	    $.each(a, function() {
	        if (o[this.name]) {
	            if (!o[this.name].push) {
	                o[this.name] = [o[this.name]];
	            }
	            o[this.name].push(this.value || '');
	        } else {
	            o[this.name] = this.value || '';
	        }
	    });
	    return o;
	};
	
}

function initMergeButton () {
	
	$('.merge-button').click(function(event) {
		var data = $.toJSON($('.uploads-form').serializeObject());
		
		//NOTE: This function has been moved to merges.js
	    postMerge(data);
	    
		event.preventDefault();
	});	
}


