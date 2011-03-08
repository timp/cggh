function initMergesFunctions () {

	initMoveJoinFunction();
	initRemoveJoinFunction();
	
	
	initSaveJoinFunction();
	initAddJoinFunction();
	initChangeDatatable1ColumnNameFunction();
	initChangeDatatable2ColumnNameFunction();

	initEditJoinFunction();	
	
	initEditResolutionsByColumnFunction();
	initSaveResolutionsByColumnFunction();
	initChangeSolutionByColumnFunction();
	
	initSaveResolutionsByRowFunction();
	initChangeSolutionByRowFunction();
	
	
	
	
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
	
	$(".save-join").click(function() {
		
		// Get data from uploads form.
		var data = $.toJSON($('.joins-form').serializeObject());
		
		//alert(data);

		
		$.ajax({
			type: 'PUT',
			data: data,
			url: '/dataMerger/data/merges/' + urlParams["merge_id"] + '/joins',
			dataType: 'json',
			success: function (data, textStatus, jqXHR) {

				retrieveJoinsAsXHTMLUsingMergeId(urlParams["merge_id"]);
					

			},
			error: function (jqXHR, textStatus, errorThrown){
	            $('.error').html("errorThrown: " + errorThrown);
	            $('.status').html("textStatus: " + textStatus);
	        } 
		});
		
	});
	
}


function retrieveJoinsAsXHTMLUsingMergeId (mergeId) {
	
	
	$.ajax({
		data: '',
		dataType: 'html',
		success: function (data, textStatus, jqXHR) { 
				$('.joins-form').html(data);
				
				//The new HTML needs to be re-bound.
				initMoveJoinFunction();
				initRemoveJoinFunction();
		},
		type: 'GET',
		url: '/dataMerger/data/merges/' + mergeId + '/joins'
	});	
}

function initChangeDatatable1ColumnNameFunction () {


	$('.new-join-form select[name=datatable_1_column_name]').change(function() {

		var row = $(this).closest("tr");
		
		//alert($(this).val());
		
		if ($(this).val() == 'CONSTANT') {
			row.next().find('td label[for="constant_1"]').css('display', 'inline');
			row.next().find('td input[name="constant_1"]').css('display', 'inline');
		} else {
			row.next().find('td label[for="constant_1"]').css('display', 'none');
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
	
}

function initChangeDatatable2ColumnNameFunction () {
	

	$('.new-join-form select[name=datatable_2_column_name]').change(function() {

		var row = $(this).closest("tr");
		
		//alert($(this).val());
		
		if ($(this).val() == 'CONSTANT') {
			row.next().find('td label[for="constant_2"]').css('display', 'inline');
			row.next().find('td input[name="constant_2"]').css('display', 'inline');
		} else {
			row.next().find('td label[for="constant_2"]').css('display', 'none');
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


function initAddJoinFunction () {
	
	$(".add-join").click(function() {
		
		//TODO: validation
		//Column name, not both nulls
	
		// Get data from new-join form.
		var data = $.toJSON($('.new-join-form').serializeObject());
	
		//alert(data);
		
		data = $.parseJSON(data);
		
		//Rather than posting, add to client-side joins (to be added using Save)
		
		
		var newJoinRow = "";
		
		newJoinRow += "<tr>";
		newJoinRow += "<td><input type=\"text\" name=\"column_number\" value=\"" + data.column_number + "\" readonly=\"readonly\"/></td>";
		
		if (data.key) {
			newJoinRow += "<td><input type=\"checkbox\" name=\"key-" + data.column_number + "\" value=\"" + data.key + "\" checked=\"checked\"/></td>";
		} else {
			newJoinRow += "<td><input type=\"checkbox\" name=\"key-" + data.column_number + "\" value=\"" + data.key + "\"/></td>";
		}
		
		if (data.datatable_1_column_name != 'NULL' && data.datatable_1_column_name != 'CONSTANT') {
			newJoinRow += "<td>";
			newJoinRow += "<input type=\"text\" name=\"database_1_column_name-" + data.column_number + "\" value=\"" + data.datatable_1_column_name + "\" readonly=\"readonly\"/>";
			newJoinRow += "<textarea>TODO: Sample of data</textarea>";
			newJoinRow += "</td>";
		} 
		else if (data.datatable_1_column_name == 'CONSTANT') {
			newJoinRow += "<td><label for=\"constant_1-" + data.column_number + "\">Constant:</label><input type=\"text\" name=\"constant_1-" + data.column_number + "\" value=\"" + data.constant_1 + "\" readonly=\"readonly\"/></td>";
		} else {
			newJoinRow += "<td>NULL</td>";
		}
		
		if (data.datatable_2_column_name != 'NULL' && data.datatable_2_column_name != 'CONSTANT') {
			newJoinRow += "<td>";
			newJoinRow += "<input type=\"text\" name=\"database_2_column_name-" + data.column_number + "\" value=\"" + data.datatable_2_column_name + "\" readonly=\"readonly\"/>";
			newJoinRow += "<texarea>TODO: Sample of data</textarea>";
			newJoinRow += "</td>";
		} 
		else if (data.datatable_2_column_name == 'CONSTANT') {
			newJoinRow += "<td><label for=\"constant_2-" + data.column_number + "\">Constant:</label><input type=\"text\" name=\"constant_2-" + data.column_number + "\" value=\"" + data.constant_2 + "\" readonly=\"readonly\"/></td>";
		} else {
			newJoinRow += "<td>NULL</td>";
		}
		
		newJoinRow += "<td><input type=\"text\" name=\"column_name\" value=\"" + data.column_name + "\"/></td>";
		
		newJoinRow += "<td><button class=\"move up\">Up<button/><button class=\"move down\">Down<button/></td>";
		newJoinRow += "<td><button class=\"remove\">Remove<button/></td>";
		
		newJoinRow += "</tr>";		
		
		
		
		var max_column_number = $('.joins-form tr input[name="column_number"]').length;
		
		//alert("max_column_number="+max_column_number);
		
		//alert("data.column_number="+data.column_number);
		
		if (data.column_number > max_column_number) {
			
			//alert("appending to end");
			
			$(".joins-form tbody").append(newJoinRow);
			
		} else {
			
			//FIXME: Move row at data.column_number down 1 (and all the rest), then insert this row at position data.column_number


			
			var row = $('.joins-form td input[name="column_number"][value="' + data.column_number + '"]').closest("tr");
			
			row.before(newJoinRow);
			
	    	var nextRow = row;
	    	var column_number = parseInt(data.column_number) + 1;
	    	
	    	//alert("column_number="+column_number);
	    	
	    	while (column_number <= max_column_number + 1) {
	    		
	    		//alert("column_number="+column_number);
	    		
	    		nextRow.find('td input[name="column_number"]').attr('value', column_number.toString());
	    		nextRow.find('td input[name|="key"]').attr('name', "key-" + column_number.toString());
	    		nextRow.find('td input[name|="datatable_1_column_name"]').attr('name', "datatable_1_column_name-" + column_number.toString());
	    		nextRow.find('td input[name|="datatable_2_column_name"]').attr('name', "datatable_2_column_name-" + column_number.toString());
	    		
	    		nextRow = nextRow.next();
	    		
	    		column_number++;
	    	}
			
	
	    	//The new HTML needs to be re-bound.
			initMoveJoinFunction();
			initRemoveJoinFunction();
			
		
		}
	});	
	
}





function initEditResolutionsByColumnFunction () {
	
	$(".edit-resolutions-by-column").click(function() {

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
		
		//FIXME:
		//window.location.href = '/dataMerger/pages/merges/' + urlParams["merge_id"] + '/resolutions-by-column';
		window.location.href = '/dataMerger/pages/merges/edit-resolutions-by-column.jsp?merge_id=' + urlParams["merge_id"];
		
	});
}


function initSaveResolutionsByColumnFunction () {
	
	$(".save-resolutions-by-column").click(function() {
		
		// Get data from uploads form.
		var data = $.toJSON($('.resolutions-by-column-form').serializeObject());
		
		//alert(data);

		
		
		$.ajax({
			type: 'PUT',
			data: data,
			url: '/dataMerger/data/merges/' + urlParams["merge_id"] + '/resolutions-by-column',
			dataType: 'json',
			success: function (data, textStatus, jqXHR) {

				retrieveResolutionsByColumnAsXHTMLUsingMergeId(urlParams["merge_id"]);
					

			},
			error: function (jqXHR, textStatus, errorThrown){
	            $('.error').html("errorThrown: " + errorThrown);
	            $('.status').html("textStatus: " + textStatus);
	        } 
		});
		
	});
	
}

function retrieveResolutionsByColumnAsXHTMLUsingMergeId (mergeId) {
	
	
	$.ajax({
		data: '',
		dataType: 'html',
		success: function (data, textStatus, jqXHR) { 
			
				$('.resolutions-by-column-form').html(data);
				initChangeSolutionByColumnFunction(); // the replaced HTML needs to be re-bound.
		},
		type: 'GET',
		url: '/dataMerger/data/merges/' + mergeId + '/resolutions-by-column'
	});	
}


function initChangeSolutionByColumnFunction () {
	

	$('.resolutions-by-column-form select[name=solution_by_column_id]').change(function() {

		var row = $(this).closest("tr");
		
		//alert($(this).val());
		
		if ($(this).val() == '4') {
			row.find('td label[for|="constant"]').css('display', 'inline');
			row.find('td input[name|="constant"]').css('display', 'inline');
		} else {
			row.find('td label[for|="constant"]').css('display', 'none');
			row.find('td input[name|="constant"]').css('display', 'none');
		}

	});	
	
}

function initEditJoinFunction () {
	
	$(".edit-join").click(function() {
		

		
		//TODO: Refactor this URL
		window.location.href = '/dataMerger/pages/merges/edit-join.jsp?merge_id=' + urlParams["merge_id"];
		
	});
	
}


function initSaveResolutionsByRowFunction () {
	
	$(".save-resolutions-by-row").click(function() {
		
		// Get data from uploads form.
		var data = $.toJSON($('.resolutions-by-row-form').serializeObject());
		
		//alert(data);
		

		$.ajax({
			type: 'PUT',
			data: data,
			url: '/dataMerger/data/merges/' + urlParams["merge_id"] + '/resolutions-by-row',
			dataType: 'json',
			success: function (data, textStatus, jqXHR) {

				retrieveResolutionsByRowAsXHTMLUsingMergeId(urlParams["merge_id"]);
					

			},
			error: function (jqXHR, textStatus, errorThrown){
	            $('.error').html("errorThrown: " + errorThrown);
	            $('.status').html("textStatus: " + textStatus);
	        } 
		});
		
	});
	
}

function initChangeSolutionByRowFunction () {
	

	$('.resolutions-by-row-form select[name=solution_by_row_id]').change(function() {

		var row = $(this).closest("tr");
		
		//alert($(this).val());
		
		if ($(this).val() == '4') {
			row.find('td label[for|="constant"]').css('display', 'inline');
			row.find('td input[name|="constant"]').css('display', 'inline');
		} else {
			row.find('td label[for|="constant"]').css('display', 'none');
			row.find('td input[name|="constant"]').css('display', 'none');
		}

	});	
	
}

function retrieveResolutionsByRowAsXHTMLUsingMergeId (mergeId) {
	
	
	$.ajax({
		data: '',
		dataType: 'html',
		success: function (data, textStatus, jqXHR) { 
			
				$('.resolutions-by-row-form').html(data);
				initChangeSolutionByRowFunction(); // the replaced HTML needs to be re-bound.
		},
		type: 'GET',
		url: '/dataMerger/data/merges/' + mergeId + '/resolutions-by-row'
	});	
}