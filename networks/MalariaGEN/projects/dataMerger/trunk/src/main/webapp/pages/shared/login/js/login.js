function initLoginFunctions () {

	initLoginFunction();
	
}

function initLoginFunction () {

	$(".loginButton").click(function() {	
		
		// Get data from files form.
		var data = $.toJSON($('.loginForm').serializeObject());
		
		//Validation: The username or password cannot be blank.
		
		var obj = jQuery.parseJSON(data);
		
		if (obj != undefined && obj.username != undefined && obj.password != undefined
				&& obj.username != "" && obj.password != "") {
		
			$.ajax({
				type: 'POST',
				data: data,
				contentType: 'application/json',
				url: '/dataMerger/data/users/authentication',
				dataType: 'json',
				success: function (data, textStatus, jqXHR) {
					
					if (data.success) {
						
						if (data.success == "true") {
							window.location.href = '/dataMerger/pages/files/';
						} else {
							$('.error').html("Access denied.");
						}
		
					} else {
						
						if (data.error) {
							$('.error').html(data.error);
						} else {
							alert("An error occurred while trying to log in.");
						}
					}
				},
				error: function (jqXHR, textStatus, errorThrown){
		            $('.error').html(errorThrown);
		        },
				beforeSend: function() { $('.authenticating-indicator').show(); },
		        complete: function() { $('.authenticating-indicator').hide(); }
			});
		
		} else {
			alert("Please provide a username and password.");
		}
		
	});
}