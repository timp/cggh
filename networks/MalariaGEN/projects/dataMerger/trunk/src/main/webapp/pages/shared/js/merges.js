function initMergesFunctions () {

	
	//TODO: move these bindings to separate functions
	
	$(".move").click(function() {
	    var row = $(this).closest("tr");
	    if ($(this).hasClass("up")) {

	    	var column_number_input_being_moved = row.find('td input[name|="column_number"]');
	    	var column_number_being_moved = parseInt(column_number_input_being_moved.val());
	    	
	    	var min_column_number = 1;
	    	var max_column_number = row.parent().find('tr').length;
	    	
	    	var column_number_being_moved_to = min_column_number;
	    	
	    	if (column_number_being_moved > min_column_number) {
	    		column_number_being_moved_to = column_number_being_moved - 1;
	    	}
	    	
	    	if (column_number_being_moved != column_number_being_moved_to) {
	    		column_number_input_being_moved.attr('value', column_number_being_moved_to);
	    		row.prev().find('td input[name|="column_number"]').attr('value', column_number_being_moved_to + 1);

		    	if (column_number_being_moved == max_column_number) {
		    		//TODO: This is not water-tight
		    		row.find('td button.down').removeAttr('disabled');
		    		row.prev().find('td button.down').attr('disabled', 'disabled');
		    		
		    	}	    		
	    		
		    	if (column_number_being_moved_to == min_column_number) {
		    		//TODO: This is not water-tight
		    		row.find('td button.up').attr('disabled', 'disabled');
		    		row.prev().find('td button.up').removeAttr('disabled');
		    	}
	    	}
	    
	        row.prev().before(row);
	        
	    } else {
	    	
	    	
	    	var column_number_input_being_moved = row.find('td input[name|="column_number"]');
	    	var column_number_being_moved = parseInt(column_number_input_being_moved.val());
	    	
	    	var min_column_number = 1;
	    	var max_column_number = row.parent().find('tr').length;
	    	
	    	
	    	var column_number_being_moved_to = max_column_number;
	    	
	    	if (column_number_being_moved < max_column_number) {
	    		column_number_being_moved_to = column_number_being_moved + 1;
	    	}
	    	
	    	if (column_number_being_moved != column_number_being_moved_to) {
	    		column_number_input_being_moved.attr('value', column_number_being_moved_to);
	    		row.next().find('td input[name|="column_number"]').attr('value', column_number_being_moved_to - 1);
	    		
		    	if (column_number_being_moved == min_column_number) {
		    		//TODO: This is not water-tight
		    		row.find('td button.up').removeAttr('disabled');
		    		row.next().find('td button.up').attr('disabled', 'disabled');
		    	}

		    	if (column_number_being_moved_to == max_column_number) {
		    		//TODO: This is not water-tight
		    		row.find('td button.down').attr('disabled', 'disabled');
		    		row.next().find('td button.down').removeAttr('disabled');
		    		
		    	}
	    	}
	    	
	        row.next().after(row);
	    }
	});

//		$(".up,.down").click(function(){
//	        var row = $(this).parents("tr:first");
//	        if ($(this).is(".up")) {
//	            row.insertBefore(row.prev());
//	        } else {
//	            row.insertAfter(row.next());
//	        }
//	    });

	$(".remove").click(function() {
	    var row = $(this).closest("tr");
	
	    //Re-order everything below this row
	    var column_number_input_being_removed = row.find('td input[name|="column_number"]');
    	var column_number_being_removed = parseInt(column_number_input_being_removed.val());
    	var min_column_number = 1;
    	var parent = row.parent();
    	var max_column_number = parent.find('tr').length;
    	
    	var nextRow = row;
    	var column_number = column_number_being_removed;
    	
    	while (column_number <= max_column_number) {
    		
    		nextRow = nextRow.next();
    		
    		nextRow.find('td input[name|="column_number"]').attr('value', column_number);
    		
    		column_number++;
    	}

        row.remove();
        
        //disable up and down for min and max rows
        parent.find('tr:first').find('td button.up').attr('disabled', 'disabled');
        parent.find('tr:last').find('td button.down').attr('disabled', 'disabled');
        
	});	
	
}

function createMergeUsingUploadIdsAsJSON () {
	
	// Get data from uploads form.
	var data = $.toJSON($('.uploads-form').serializeObject());
	
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
