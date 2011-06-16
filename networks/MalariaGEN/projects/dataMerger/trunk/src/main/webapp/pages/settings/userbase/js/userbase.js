function initUserbaseFunctions () {

	initCreateUserFunction();
	
}
function initCreateUserFunction () {

	$(".createUserButton").click(function() {

		// Get data from uploads form.
		var data = $.toJSON($('.new-user-form').serializeObject());
		
		//alert(data);

		var obj = jQuery.parseJSON(data);
		
		if (obj != undefined 
				&& obj.username != undefined 
				&& obj.password != undefined 
				&& obj.username != "" 
				&& obj.password != "") {
				
			
			$.ajax({
				type: 'PUT',
				data: data,
				url: '/dataMerger/data/userbases',
				contentType: 'application/json',
				dataType: 'json',
				success: function (data, textStatus, jqXHR) {
					
					retrieveUsersAsDecoratedXHTMLTable();
	
				},
				error: function (jqXHR, textStatus, errorThrown){
		            $('.error').html(errorThrown);
		            $('.status').html(textStatus);
		        },
				beforeSend: function() { $('.creating-indicator').show(); },
		        complete: function() { $('.creating-indicator').hide(); }
			});
		
		} else {
			alert("Username and password cannot be blank.");
		}
		
	});
	
}

function retrieveUsersAsDecoratedXHTMLTable () {
	
	$.ajax({
			type: 'GET',
			url: '/dataMerger/data/userbases',
			data: '',
			dataType: 'html',
			success: function (data, textStatus, jqXHR) { 
				$('.users').html(data); 
				//Need to re-bind the new HTML
				initUserbaseFunctions();
			},
			error: function (jqXHR, textStatus, errorThrown){
	            $('.error').html(errorThrown);
	            $('.status').html(textStatus);
	        }

		});
	
}