function initJoinsFunctions () {

	initMoveJoinFunction();
	initRemoveJoinFunction();
	initSaveJoinFunction();
	initAddJoinFunction();
	initChangeDatatable1ColumnNameFunction();
	initChangeDatatable2ColumnNameFunction();

	initEditResolutionsByColumnFunction();	
	initExportFunction();
}




function initMoveJoinFunction() {
	
	$(".move").click(function() {
	    var row = $(this).closest("tr");
	    if ($(this).hasClass("up")) {

	    	//Note: Don't trust the column_number in the DOM (bug). Use row index instead.
	    	// i.e. don't depend on column_number_input_being_moved.val(), use row.index() + 1.
	    	
	    	var column_number_input_being_moved = row.find('td input[name="column_number"]');
	    	var column_number_being_moved = row.index() + 1;
	    	
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
	    		row.find('td input[name|="constant_1"]').attr('name', "constant_1-" + parseInt(column_number_being_moved_to).toString());
	    		row.find('td input[name|="constant_2"]').attr('name', "constant_2-" + parseInt(column_number_being_moved_to).toString());
	    		
	    		if (column_number_being_moved_to % 2 == 0) {
	    			row.removeClass("odd").addClass("even");
	    		} else {
	    			row.removeClass("even").addClass("odd");
	    		}
	    		
	    		row.prev().find('td input[name="column_number"]').attr('value', column_number_being_moved_to + 1);
	    		row.prev().find('td input[name|="key"]').attr('name', "key-" + (parseInt(column_number_being_moved_to) + 1).toString());
	    		row.prev().find('td input[name|="datatable_1_column_name"]').attr('name', "datatable_1_column_name-" + (parseInt(column_number_being_moved_to) + 1).toString());
	    		row.prev().find('td input[name|="datatable_2_column_name"]').attr('name', "datatable_2_column_name-" + (parseInt(column_number_being_moved_to) + 1).toString());
	    		row.prev().find('td input[name|="constant_1"]').attr('name', "constant_1-" + (parseInt(column_number_being_moved_to) + 1).toString());
	    		row.prev().find('td input[name|="constant_2"]').attr('name', "constant_2-" + (parseInt(column_number_being_moved_to) + 1).toString());

	    		if (column_number_being_moved_to % 2 == 0) {
	    			row.prev().removeClass("even").addClass("odd");
	    		} else {
	    			row.prev().removeClass("odd").addClass("even");
	    		}
	    		
		    	if (column_number_being_moved == max_column_number) {
		    		//TODO: This is not water-tight
		    		row.find('td button.down').removeAttr('disabled');
		    		row.prev().find('td button.down').attr('disabled', 'disabled');
		    		row.removeClass("last");
		    		row.prev().addClass("last");
		    	}	    		
	    		
		    	if (column_number_being_moved_to == min_column_number) {
		    		//TODO: This is not water-tight
		    		row.find('td button.up').attr('disabled', 'disabled');
		    		row.prev().find('td button.up').removeAttr('disabled');
		    		row.addClass("first");
		    		row.prev().removeClass("first");
		    	}
	    	}
	    
	        row.prev().before(row);
	        
	    } else {
	    	
	    	
	    	var column_number_input_being_moved = row.find('td input[name="column_number"]');
	    	var column_number_being_moved = row.index() + 1;
	    	
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
	    		row.find('td input[name|="constant_1"]').attr('name', "constant_1-" + parseInt(column_number_being_moved_to).toString());
	    		row.find('td input[name|="constant_2"]').attr('name', "constant_2-" + parseInt(column_number_being_moved_to).toString());

	    		if (column_number_being_moved_to % 2 == 0) {
	    			row.removeClass("odd").addClass("even");
	    		} else {
	    			row.removeClass("even").addClass("odd");
	    		}
	    		
	    		row.next().find('td input[name="column_number"]').attr('value', column_number_being_moved_to - 1);
	    		row.next().find('td input[name|="key"]').attr('name', "key-" + (parseInt(column_number_being_moved_to) - 1).toString());
	    		row.next().find('td input[name|="datatable_1_column_name"]').attr('name', "datatable_1_column_name-" + (parseInt(column_number_being_moved_to) - 1).toString());
	    		row.next().find('td input[name|="datatable_2_column_name"]').attr('name', "datatable_2_column_name-" + (parseInt(column_number_being_moved_to) - 1).toString());
	    		row.next().find('td input[name|="constant_1"]').attr('name', "constant_1-" + (parseInt(column_number_being_moved_to) - 1).toString());
	    		row.next().find('td input[name|="constant_2"]').attr('name', "constant_2-" + (parseInt(column_number_being_moved_to) - 1).toString());

	    		if (column_number_being_moved_to % 2 == 0) {
	    			row.next().removeClass("even").addClass("odd");
	    		} else {
	    			row.next().removeClass("odd").addClass("even");
	    		}
	    		
		    	if (column_number_being_moved == min_column_number) {
		    		//TODO: This is not water-tight
		    		row.find('td button.up').removeAttr('disabled');
		    		row.next().find('td button.up').attr('disabled', 'disabled');
		    		row.removeClass("first");
		    		row.next().addClass("first");
		    	}

		    	if (column_number_being_moved_to == max_column_number) {
		    		//TODO: This is not water-tight
		    		row.find('td button.down').attr('disabled', 'disabled');
		    		row.next().find('td button.down').removeAttr('disabled');
		    		row.addClass("last");
		    		row.next().removeClass("last");
		    		
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
    		nextRow.find('td input[name|="constant_1"]').attr('name', "constant_1-" + column_number.toString());
    		nextRow.find('td input[name|="constant_2"]').attr('name', "constant_2-" + column_number.toString());
    		
    		if (column_number % 2 == 0) {
    			nextRow.removeClass('odd').addClass('even');
    		} else {
    			nextRow.removeClass('even').addClass('odd');
    		}
    		
    		column_number++;
    	}

        row.remove();
        
        //disable up and down for min and max rows
        parent.find('tr:first').find('td button.up').attr('disabled', 'disabled');
        parent.find('tr:last').find('td button.down').attr('disabled', 'disabled');
        parent.find('tr:first').addClass('first');
        parent.find('tr:last').addClass('last');
        
        syncNewJoinColumnNumberOptions();
        
	});	

}

function initSaveJoinFunction () {
	
	$(".save-join").click(function() {
		
		// Get data from uploads form.
		var data = $.toJSON($('.joins-form').serializeObject());
		
		//
		//alert(data);

		
		$.ajax({
			type: 'PUT',
			data: data,
			url: '/dataMerger/data/merges/' + urlParams["merge_id"] + '/joins',
			dataType: 'json',
			success: function (data, textStatus, jqXHR) {

				//v1. Just reload the page.
				document.location.reload();
				
				//retrieveJoinsAsXHTMLUsingMergeId(urlParams["merge_id"]);
				
				//TODO: 
				//retrieveMergeSummaryAsXHTMLUsingMergeId(urlParams["merge_id"]);

			},
			error: function (jqXHR, textStatus, errorThrown){
	            $('.error').html("errorThrown: " + errorThrown);
	            $('.status').html("textStatus: " + textStatus);
	        },
			beforeSend: function() { $('.saving-indicator').show(); },
	        complete: function() { $('.saving-indicator').hide(); }
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
		
		
		if (
		
				($(this).val() == 'NULL' || $(this).val() == 'CONSTANT')
				||
				(row.find('select[name=datatable_2_column_name]').val() == 'NULL' || row.find('select[name=datatable_2_column_name]').val() == 'CONSTANT')
		
		) {
			
			row.find('input[name=key]').attr("disabled", "disabled").removeAttr("checked");
			
		} else {
			
			row.find('input[name=key]').removeAttr("disabled");
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

		
		if (
				
				($(this).val() == 'NULL' || $(this).val() == 'CONSTANT')
				||
				(row.find('select[name=datatable_1_column_name]').val() == 'NULL' || row.find('select[name=datatable_1_column_name]').val() == 'CONSTANT')
		
		) {
			
			row.find('input[name=key]').attr("disabled", "disabled").removeAttr("checked");
			
		} else {
			
			row.find('input[name=key]').removeAttr("disabled");
		}
		
	});
	
}


function initAddJoinFunction () {
	
	$(".add-join").click(function() {
		
		//FIXME: This needs to be kept in sync with the mergeFunctions HTML
		
		//TODO: validation
		//Column name, not both nulls
	
		// Get data from new-join form.
		var data = $.toJSON($('.new-join-form').serializeObject());
	
		//alert(data);
		
		data = $.parseJSON(data);
		
		//Rather than posting, add to client-side joins (to be added using Save)
		
		var min_column_number = 1;
		var max_column_number = $('.joins-form tbody tr').length;
		
		//alert("1 max_column_number: " + max_column_number);
		
		var newJoinRow = "";

		var rowStripeClassName = ""; 
		var rowFirstClassName = "";
		var rowLastClassName = ""; 		

		if (data.column_number % 2 == 0) {
			rowStripeClassName = "even ";
		} else {
			rowStripeClassName = "odd ";
		}		
		
		if (data.column_number == min_column_number) {
			rowFirstClassName = "first ";
		} else {
			rowFirstClassName = "";
		}		
		
		if (data.column_number > max_column_number) {
			rowLastClassName = "last ";
		} else {
			rowLastClassName = "";
		}
		

		
		newJoinRow += "<tr class=\"" + rowStripeClassName + rowFirstClassName + rowLastClassName + "\">";
		
		newJoinRow += "<td class=\"column_number-container\"><input type=\"text\" name=\"column_number\" value=\"" + data.column_number + "\" readonly=\"readonly\"/></td>";
		
		if (
				data.datatable_1_column_name == 'NULL' || data.datatable_1_column_name == 'CONSTANT'
				||
				data.datatable_2_column_name == 'NULL' || data.datatable_2_column_name == 'CONSTANT'
			) {
			
			newJoinRow += "<td class=\"key-container\"><input type=\"checkbox\" name=\"key-" + data.column_number + "\" value=\"" + data.key + "\" disabled=\"disabled\"/></td>";
			
		} else {
			
			if (data.key) {
				newJoinRow += "<td class=\"key-container\"><input type=\"checkbox\" name=\"key-" + data.column_number + "\" value=\"" + data.key + "\" checked=\"checked\"/></td>";
			} else {
				newJoinRow += "<td class=\"key-container\"><input type=\"checkbox\" name=\"key-" + data.column_number + "\" value=\"" + data.key + "\"/></td>";
			}
		}
		
		if (data.datatable_1_column_name != 'NULL' && data.datatable_1_column_name != 'CONSTANT') {
			newJoinRow += "<td class=\"datatable_1_column_name-container\">";
			newJoinRow += "<input type=\"text\" name=\"datatable_1_column_name-" + data.column_number + "\" value=\"" + data.datatable_1_column_name + "\" readonly=\"readonly\"/>";

			//TODO
			//newJoinRow += "<textarea>TODO: Sample of data</textarea>";
			
			newJoinRow += "</td>";
		} 
		else if (data.datatable_1_column_name == 'CONSTANT') {
			newJoinRow += "<td class=\"constant_1-container\"><label for=\"constant_1-" + data.column_number + "\">Constant:</label><input type=\"text\" name=\"constant_1-" + data.column_number + "\" value=\"" + data.constant_1 + "\" readonly=\"readonly\"/></td>";
		} else {
			newJoinRow += "<td class=\"null-container\">NULL</td>";
		}
		
		if (data.datatable_2_column_name != 'NULL' && data.datatable_2_column_name != 'CONSTANT') {
			newJoinRow += "<td class=\"datatable_2_column_name-container\">";
			newJoinRow += "<input type=\"text\" name=\"datatable_2_column_name-" + data.column_number + "\" value=\"" + data.datatable_2_column_name + "\" readonly=\"readonly\"/>";
			
			//TODO
			//newJoinRow += "<texarea>TODO: Sample of data</textarea>";
			
			newJoinRow += "</td>";
		} 
		else if (data.datatable_2_column_name == 'CONSTANT') {
			newJoinRow += "<td class=\"constant_2-container\"><label for=\"constant_2-" + data.column_number + "\">Constant:</label><input type=\"text\" name=\"constant_2-" + data.column_number + "\" value=\"" + data.constant_2 + "\" readonly=\"readonly\"/></td>";
		} else {
			newJoinRow += "<td class=\"null-container\">NULL</td>";
		}
		
		newJoinRow += "<td class=\"column_name-container\"><input type=\"text\" name=\"column_name\" value=\"" + data.column_name + "\"/></td>";
		
		newJoinRow += "<td class=\"move-row-buttons-container\"><button class=\"move up\"><img src=\"/dataMerger/pages/shared/png/up.png\" title=\"Up\" /></button><button class=\"move down\"><img src=\"/dataMerger/pages/shared/png/down.png\" title=\"Down\" /></button></td>";
		newJoinRow += "<td class=\"remove-button-container\"><button class=\"remove\">Remove</button></td>";
		
		newJoinRow += "</tr>";		
		
		
		
		
		
		//alert("max_column_number="+max_column_number);
		
		//alert("data.column_number="+data.column_number);
		
		if (data.column_number > max_column_number) {
			
			//alert("appending to end");

			//TODO: Remove the last class from the row this is replacing
			$('.joins-form tr:last').removeClass("last");

			
			$(".joins-form tbody").append(newJoinRow);
			
			

			
		} else {
			
			// Move row at data.column_number down 1 (and all the rest), then insert this row at position data.column_number

			//alert("data.column_number " + data.column_number);
			
			//FIXME FIXME FIXME
			// The selector momentarily applies to two elements, both with the same column_number
	
	    	
	    	var nextRow = $('.joins-form tbody tr:nth-child(' + data.column_number + ')');
	    	
	    	var nextRow_new_column_number = parseInt(data.column_number) + 1;
	    	
	    	while (nextRow_new_column_number <= max_column_number + 1) {
	    		
	    		//alert("max_column_number="+max_column_number);
	    		
	    		nextRow.find('td input[name="column_number"]').val(nextRow_new_column_number.toString());
	    		nextRow.find('td input[name|="key"]').attr('name', "key-" + nextRow_new_column_number.toString());
	    		nextRow.find('td input[name|="datatable_1_column_name"]').attr('name', "datatable_1_column_name-" + nextRow_new_column_number.toString());
	    		nextRow.find('td input[name|="datatable_2_column_name"]').attr('name', "datatable_2_column_name-" + nextRow_new_column_number.toString());
	    		nextRow.find('td input[name|="constant_1"]').attr('name', "constant_1-" + nextRow_new_column_number.toString());
	    		nextRow.find('td input[name|="constant_2"]').attr('name', "constant_2-" + nextRow_new_column_number.toString());
	    		
	    		if (nextRow_new_column_number % 2 == 0) {
	    			nextRow.removeClass('odd').addClass('even');
	    		} else {
	    			nextRow.removeClass('even').addClass('odd');
	    		}
	    		
	    		nextRow = nextRow.next();
	    		
	    		nextRow_new_column_number++;
	    	}

	    	//Note: FF3 + Chrome (oddly enough not IE8) had bug with setting/selecting column_name value
	    	
	    	var rowToInsertBefore = $('.joins-form tbody tr:nth-child(' + data.column_number + ')');
	    	
	    	rowToInsertBefore.removeClass("first");
	    	
	    	rowToInsertBefore.before(newJoinRow);
	    	
		}
		

		//The new HTML needs to be re-bound.
		initMoveJoinFunction();
		initRemoveJoinFunction();
		
		// Get a new join (and re-bind it)
		retrieveNewJoinAsXHTML();

		
	});	
	
}


function initEditResolutionsByColumnFunction () {
	
	$(".edit-resolutions-by-column").click(function() {
		
		//FIXME:
		//window.location.href = '/dataMerger/pages/merges/' + urlParams["merge_id"] + '/resolutions-by-column';
		window.location.href = '/dataMerger/pages/merges/resolutions/by-column?merge_id=' + urlParams["merge_id"];
		
	});
}

function retrieveNewJoinAsXHTML () {

	$.ajax({
		type: 'GET',
		url: '/dataMerger/data/merges/' + urlParams["merge_id"] + '/joins/join',
		data: '',
		  //FIXME: This content-type isn't necessary, just a test
		contentType: 'application/json',
		dataType: 'html',
		success: function (data, textStatus, jqXHR) {
			
				$('.new-join-table-container').html(data);
				
				//The new HTML needs to be re-bound.
				initAddJoinFunction();
				initChangeDatatable1ColumnNameFunction();
				initChangeDatatable2ColumnNameFunction();
				syncNewJoinColumnNumberOptions();
		},
		error: function (jqXHR, textStatus, errorThrown){
            $('.error').html("errorThrown: " + errorThrown);
            $('.status').html("textStatus: " + textStatus);
        }
	});	
	
}


function syncNewJoinColumnNumberOptions () {

	if ($('.new-join-form select[name="column_number"] option').length <= $('.joins-form input[name="column_number"]').length) {
		
		$('.new-join-form select[name="column_number"]').find('option[value="' + $('.new-join-form select[name="column_number"] option').length + '"]').text( ($('.new-join-form select[name="column_number"] option').length - 1) + ' --> <-- ' + $('.new-join-form select[name="column_number"] option').length);
		$('.new-join-form select[name="column_number"]').find('option[value="' + $('.new-join-form select[name="column_number"] option').length + '"]').removeAttr('selected');
		
		while ($('.new-join-form select[name="column_number"] option').length <= $('.joins-form input[name="column_number"]').length) {
			
			$('.new-join-form select[name="column_number"]').append('<option value="' + ($('.new-join-form select[name="column_number"] option').length + 1) + '">' + $('.new-join-form select[name="column_number"] option').length + ' --> <-- ' + ($('.new-join-form select[name="column_number"] option').length + 1) + '</option>');
		}
		
		$('.new-join-form select[name="column_number"]').find('option[value="' + $('.new-join-form select[name="column_number"] option').length + '"]').text('place last');
		$('.new-join-form select[name="column_number"]').find('option[value="' + $('.new-join-form select[name="column_number"] option').length + '"]').attr('selected', 'selected');
	}

	// if deleted
	if ($('.new-join-form select[name="column_number"] option').length > ($('.joins-form input[name="column_number"]').length + 1)) {
		
		$('.new-join-form select[name="column_number"]').find('option[value="' + $('.new-join-form select[name="column_number"] option').length + '"]').text( ($('.new-join-form select[name="column_number"] option').length - 1) + ' --> <-- ' + $('.new-join-form select[name="column_number"] option').length);
		$('.new-join-form select[name="column_number"]').find('option[value="' + $('.new-join-form select[name="column_number"] option').length + '"]').removeAttr('selected');
		
		while ($('.new-join-form select[name="column_number"] option').length > ($('.joins-form input[name="column_number"]').length + 1)) {
			
			$('.new-join-form select[name="column_number"] option:last').remove();
		}
		
		$('.new-join-form select[name="column_number"]').find('option[value="' + $('.new-join-form select[name="column_number"] option').length + '"]').text('place last');
		$('.new-join-form select[name="column_number"]').find('option[value="' + $('.new-join-form select[name="column_number"] option').length + '"]').attr('selected', 'selected');
	}	
	
}

function retrieveMergeSummaryAsXHTMLUsingMergeId (mergeId) {
	
	//TODO
	//Currently just reloading page. Might not be worth doing separate AJAX calls.
}

function initExportFunction () {
	
	$(".export-button").click(function() {

		//alert($("input[name=mergedFileFilename]").val());
		
		if (urlParams["merge_id"] != null) {
			
			if ($("input[name=mergedFileFilename]") != null && $("input[name=mergedFileFilename]").val() != "") {
			
				var dataObj = {mergedFileFilename : $("input[name=mergedFileFilename]").val()};
				
				$.ajax({
					type: 'POST',
					contentType: 'application/json',
					data: $.toJSON(dataObj),
					url: '/dataMerger/data/merges/' + urlParams["merge_id"] + '/exports',
					dataType: 'json',
					success: function (data, textStatus, jqXHR) {
						
						if (data.id) {
							
							window.location.href = '/dataMerger/pages/exports';
			
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
					beforeSend: function() { $('.exporting-indicator').show(); },
			        complete: function() { $('.exporting-indicator').hide(); }
				});
			
			} else {
				alert("The filename has not been specified.");
			}
				
		} else {
			
			alert("The merge has not been specified.");
		}
		
	});
	
}