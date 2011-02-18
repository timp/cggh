function postMerge (data) {
	
	//TODO: Validate exactly two checkboxes selected.
	
	$.ajax({
		data: data,
		dataType: 'json',
		success: function (data, textStatus, jqXHR) {
			
			if (data.id) {
				
				//TODO: This URL is horrid.
				window.location.href = '/dataMerger/pages/merges/edit-join.jsp?merge_id=' + data.id;

			} else {
				alert("data: " + data);
				alert("data.id: " + data.id);
				$('.status').html("textStatus: " + textStatus);
			}
		},
		type: 'POST',
		url: '/dataMerger/data/merges',
		error: function (jqXHR, textStatus, errorThrown){
            $('.error').html("errorThrown: " + errorThrown);
            $('.status').html("textStatus: " + textStatus);
        } 
	});
	
}