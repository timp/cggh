function initMergesFunctions () {

	initMoveJoinFunction();
	initRemoveJoinFunction();
	initSaveJoinFunction();
	initAddJoinFunction();
	
	//TODO: move to separate functions
	
	$('.new-join-form select[name=datatable_1_column_name]').change(function() {

		var row = $(this).closest("tr");
		
		//alert($(this).val());
		
		if ($(this).val() == 'CONSTANT') {
			row.next().find('th label[for="constant_1"]').css('display', 'inline');
			row.next().find('td input[name="constant_1"]').css('display', 'inline');
		} else {
			row.next().find('th label[for="constant_1"]').css('display', 'none');
			row.next().find('td input[name="constant_1"]').css('display', 'none');
		}
		
		if (
				(
				(row.find('select[name=datatable_2_column_name]').val() == 'NULL' || row.find('select[name=datatable_2_column_name]').val() == 'CONSTANT')
				&&
				row.find('input[name=column_name]').val() == ''
				&&
				($(this).val() != 'NULL' && $(this).val() != 'CONSTANT')
				)
				||
				($(this).val() != 'NULL' && $(this).val() != 'CONSTANT' && $(this).val() == row.find('select[name=datatable_2_column_name]').val())
		) {
			
			row.find('input[name=column_name]').attr('value', $(this).val());
		}
		
	});
	
	$('.new-join-form select[name=datatable_2_column_name]').change(function() {

		var row = $(this).closest("tr");
		
		//alert($(this).val());
		
		if ($(this).val() == 'CONSTANT') {
			row.next().find('th label[for="constant_2"]').css('display', 'inline');
			row.next().find('td input[name="constant_2"]').css('display', 'inline');
		} else {
			row.next().find('th label[for="constant_2"]').css('display', 'none');
			row.next().find('td input[name="constant_2"]').css('display', 'none');
		}

		
		if (
				(
				(row.find('select[name=datatable_1_column_name]').val() == 'NULL' || row.find('select[name=datatable_1_column_name]').val() == 'CONSTANT')
				&&
				row.find('input[name=column_name]').val() == ''
				&&
				($(this).val() != 'NULL' && $(this).val() != 'CONSTANT')
				)
				||
				($(this).val() != 'NULL' && $(this).val() != 'CONSTANT' && $(this).val() == row.find('select[name=datatable_1_column_name]').val())
		) {
			
			row.find('input[name=column_name]').attr('value', $(this).val());
		}		
		
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


function initMoveJoinFunction() {
	
	$(".move").click(function() {
	    var row = $(this).closest("tr");
	    if ($(this).hasClass("up")) {

	    	var column_number_input_being_moved = row.find('td input[name="column_number"]');
	    	var column_number_being_moved = parseInt(column_number_input_being_moved.val());
	    	
	    	var min_column_number = 1;
	    	var max_column_number = row.parent().find('tr').length;
	    	
	    	var column_number_being_moved_to = min_column_number;
	    	
	    	if (column_number_being_moved > min_column_number) {
	    		column_number_being_moved_to = column_number_being_moved - 1;
	    	}
	    	
	    	if (column_number_being_moved != column_number_being_moved_to) {
	    		
	    		column_number_input_being_moved.attr('value', column_number_being_moved_to);
	    		row.find('td input[name|="key"]').attr('name', "key-" + parseInt(column_number_being_moved_to).toString());
	    		row.find('td input[name|="datatable_1_column_name"]').attr('name', "datatable_1_column_name-" + parseInt(column_number_being_moved_to).toString());
	    		row.find('td input[name|="datatable_2_column_name"]').attr('name', "datatable_2_column_name-" + parseInt(column_number_being_moved_to).toString());
	    		
	    		row.prev().find('td input[name="column_number"]').attr('value', column_number_being_moved_to + 1);
	    		row.prev().find('td input[name|="key"]').attr('name', "key-" + (parseInt(column_number_being_moved_to) + 1).toString());
	    		row.prev().find('td input[name|="datatable_1_column_name"]').attr('name', "datatable_1_column_name-" + (parseInt(column_number_being_moved_to) + 1).toString());
	    		row.prev().find('td input[name|="datatable_2_column_name"]').attr('name', "datatable_2_column_name-" + (parseInt(column_number_being_moved_to) + 1).toString());

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
	    	
	    	
	    	var column_number_input_being_moved = row.find('td input[name="column_number"]');
	    	var column_number_being_moved = parseInt(column_number_input_being_moved.val());
	    	
	    	var min_column_number = 1;
	    	var max_column_number = row.parent().find('tr').length;
	    	
	    	
	    	var column_number_being_moved_to = max_column_number;
	    	
	    	if (column_number_being_moved < max_column_number) {
	    		column_number_being_moved_to = column_number_being_moved + 1;
	    	}
	    	
	    	if (column_number_being_moved != column_number_being_moved_to) {
	    		
	    		column_number_input_being_moved.attr('value', column_number_being_moved_to);
	    		row.find('td input[name|="key"]').attr('name', "key-" + parseInt(column_number_being_moved_to).toString());
	    		row.find('td input[name|="datatable_1_column_name"]').attr('name', "datatable_1_column_name-" + parseInt(column_number_being_moved_to).toString());
	    		row.find('td input[name|="datatable_2_column_name"]').attr('name', "datatable_2_column_name-" + parseInt(column_number_being_moved_to).toString());
	    		
	    		row.next().find('td input[name="column_number"]').attr('value', column_number_being_moved_to - 1);
	    		row.next().find('td input[name|="key"]').attr('name', "key-" + (parseInt(column_number_being_moved_to) - 1).toString());
	    		row.next().find('td input[name|="datatable_1_column_name"]').attr('name', "datatable_1_column_name-" + (parseInt(column_number_being_moved_to) - 1).toString());
	    		row.next().find('td input[name|="datatable_2_column_name"]').attr('name', "datatable_2_column_name-" + (parseInt(column_number_being_moved_to) - 1).toString());
	    		
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

}

function initRemoveJoinFunction () {
	
	//FIXME: update insertion selector
	
	$(".remove").click(function() {
	    var row = $(this).closest("tr");
	
	    //Re-order everything below this row
	    var column_number_input_being_removed = row.find('td input[name="column_number"]');
    	var column_number_being_removed = parseInt(column_number_input_being_removed.val());
    	var min_column_number = 1;
    	var parent = row.parent();
    	var max_column_number = parent.find('tr').length;
    	
    	var nextRow = row;
    	var column_number = column_number_being_removed;
    	
    	while (column_number <= max_column_number) {
    		
    		nextRow = nextRow.next();
    		
    		nextRow.find('td input[name="column_number"]').attr('value', column_number);
    		nextRow.find('td input[name|="key"]').attr('name', "key-" + column_number.toString());
    		nextRow.find('td input[name|="datatable_1_column_name"]').attr('name', "datatable_1_column_name-" + column_number.toString());
    		nextRow.find('td input[name|="datatable_2_column_name"]').attr('name', "datatable_2_column_name-" + column_number.toString());
    		
    		column_number++;
    	}

        row.remove();
        
        //disable up and down for min and max rows
        parent.find('tr:first').find('td button.up').attr('disabled', 'disabled');
        parent.find('tr:last').find('td button.down').attr('disabled', 'disabled');
        
	});	

}

function initSaveJoinFunction () {
	
	$(".save.join").click(function() {
		
		// Get data from uploads form.
		var data = $.toJSON($('.joins-form').serializeObject());
		
		//alert(data);
		
		//TODO: Factor this out
		var urlParams = {};
		(function () {
		    var e,
		        a = /\+/g,  // Regex for replacing addition symbol with a space
		        r = /([^&=]+)=?([^&]*)/g,
		        d = function (s) { return decodeURIComponent(s.replace(a, " ")); },
		        q = window.location.search.substring(1);

		    while (e = r.exec(q))
		       urlParams[d(e[1])] = d(e[2]);
		})();
		
		//			urlParams = {
		//			    enc: " Hello ",
		//			    i: "main",
		//			    mode: "front",
		//			    sid: "de8d49b78a85a322c4155015fdce22c4",
		//			    empty: ""
		//			}
		//
		//			alert(urlParams["mode"]);
		//			// -> "front"
		
		
		
		$.ajax({
			type: 'PUT',
			data: data,
			url: '/dataMerger/data/merges/' + urlParams["merge_id"] + '/joins',
			dataType: 'json',
			success: function (data, textStatus, jqXHR) {

				retrieveJoinsAsHTMLUsingMergeId(urlParams["merge_id"]);
					

			},
			error: function (jqXHR, textStatus, errorThrown){
	            $('.error').html("errorThrown: " + errorThrown);
	            $('.status').html("textStatus: " + textStatus);
	        } 
		});
		
	});
	
}


function retrieveJoinsAsHTMLUsingMergeId (mergeId) {
	
	
	$.ajax({
		data: '',
		dataType: 'html',
		success: function (data, textStatus, jqXHR) { $('.joins').html(data); },
		type: 'GET',
		url: '/dataMerger/data/merges/' + mergeId + '/joins'
	});	
}


function initAddJoinFunction () {
	
	$(".add.join").click(function() {
		
		//TODO: validation
		//Column name, not both nulls
	
		// Get data from new-join form.
		var data = $.toJSON($('.new-join-form').serializeObject());
	
		alert(data);
		
		data = $.parseJSON(data);
		
		//Rather than posting, add to client-side joins (to be added using Save)
		
		var newJoinRow = "";
		
		//FIXME: Move row at data.column_number down 1 (and all the rest), then insert this row at position data.column_number
		
		newJoinRow += "<tr>";
		newJoinRow += "<td><input type=\"text\" name=\"column_number\" value=\"" + data.column_number + "\" readonly=\"readonly\"/></td>";
		
		//FIXME
		if (data.key) {
			newJoinRow += "<td><input type=\"checkbox\" name=\"key-" + 4000 + "\" value=\"" + data.key + "\" checked=\"checked\"/></td>";
		} else {
			newJoinRow += "<td><input type=\"checkbox\" name=\"key-" + 4000 + "\" value=\"" + data.key + "\"/></td>";
		}
		
		if (data.datatable_1_column_name != 'NULL' && data.datatable_1_column_name != 'CONSTANT') {
			newJoinRow += "<td><input type=\"text\" name=\"database_1_column_name-" + 4000 + "\" value=\"" + data.datatable_1_column_name + "\" readonly=\"readonly\"/></td>";
			newJoinRow += "<td>TODO: Sample of data</td>";
		} 
		else if (data.datatable_1_column_name == 'CONSTANT') {
			newJoinRow += "<td><label for=\"constant_1-" + 4000 + "\">Constant:</label><input type=\"text\" name=\"constant_1-" + 4000 + "\" value=\"" + data.constant_1 + "\" readonly=\"readonly\"/></td>";
			newJoinRow += "<td></td>";
		} else {
			newJoinRow += "<td>NULL</td>";
			newJoinRow += "<td></td>";
		}
		
		if (data.datatable_2_column_name != 'NULL' && data.datatable_2_column_name != 'CONSTANT') {
			newJoinRow += "<td><input type=\"text\" name=\"database_2_column_name-" + 4000 + "\" value=\"" + data.datatable_2_column_name + "\" readonly=\"readonly\"/></td>";
			newJoinRow += "<td>TODO: Sample of data</td>";
		} 
		else if (data.datatable_2_column_name == 'CONSTANT') {
			newJoinRow += "<td><label for=\"constant_2-" + 4000 + "\">Constant:</label><input type=\"text\" name=\"constant_2-" + 4000 + "\" value=\"" + data.constant_2 + "\" readonly=\"readonly\"/></td>";
			newJoinRow += "<td></td>";
		} else {
			newJoinRow += "<td>NULL</td>";
			newJoinRow += "<td></td>";
		}
		
		newJoinRow += "<td><input type=\"text\" name=\"column_name-\" value=\"" + data.column_name + "\"/></td>";
		
		newJoinRow += "<td><button class=\"move up\">Up<button/><button class=\"move down\">Down<button/></td>";
		newJoinRow += "<td><button class=\"remove\">Remove<button/></td>";
		
		newJoinRow += "</tr>";
		
		$(".joins tbody").append(newJoinRow);
		
		
	});	
	
}
