function initUploadsScripts () {

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
	
	$('.merge-button').click(function(event) {
		var data = $.toJSON($('.uploads-form').serializeObject());
	    postMerge(data);
		event.preventDefault();
	});	

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

function postMerge (data) {
	
	//TODO: Validate exactly two checkboxes selected.
	
	$.ajax({
		data: data,
		dataType: 'json',
		success: function (data, textStatus, jqXHR) { 
			window.location.href = '/dataMerger/pages/merges/edit-join.jsp?id=' + data.id;
			//$('.status').html("textStatus: " + textStatus);
		},
		type: 'POST',
		url: '/dataMerger/data/merges',
		error: function (jqXHR, textStatus, errorThrown){
            $('.error').html("errorThrown: " + errorThrown);
            $('.status').html("textStatus: " + textStatus);
        } 
	});
	
}
