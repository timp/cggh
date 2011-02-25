function postMerge (data) {
	
	//TODO: Validate exactly two checkboxes selected.
	
	$.ajax({
		type: 'POST',
		data: data,
		url: '/dataMerger/data/merges',
		dataType: 'json',
		success: function (data, textStatus, jqXHR) {
			
			if (data.id) {
				
				//TODO: This URL is ugly.
				//TODO: GET /dataMerger/pages/merges/[id]/join
				window.location.href = '/dataMerger/pages/merges/edit-join.jsp?merge_id=' + data.id;

			} else {
				alert("data: " + data);
				alert("data.id: " + data.id);
				$('.status').html("textStatus: " + textStatus);
			}
		},
		error: function (jqXHR, textStatus, errorThrown){
            $('.error').html("errorThrown: " + errorThrown);
            $('.status').html("textStatus: " + textStatus);
        } 
	});
	
}

function updateJoinColumnNumberByJoinId(joinId, columnNumber) {
	
	$.ajax({
		type: 'PUT',
		data: "{\"columnNumber\":\"" + columnNumber + "\"}",
		url: '/dataMerger/data/joins/' + joinId,
		dataType: 'json',
		success: function (data, textStatus, jqXHR) {
			
				alert("data: " + data);
				alert("data.id: " + data.id);
				$('.status').html("textStatus: " + textStatus);

		},
		error: function (jqXHR, textStatus, errorThrown){
            $('.error').html("errorThrown: " + errorThrown);
            $('.status').html("textStatus: " + textStatus);
        } 
	});
	
}