function initLoginFunctions () {

	initLoginFunction();
	
}

function initLoginFunction () {

	$(".loginButton").click(function() {	
		
		// Get data from files form.
		var data = $.toJSON($('.loginForm').serializeObject());
		
		//Validate exactly two checkboxes selected.
		
		var obj = jQuery.parseJSON(data);
		
		if (obj != undefined && obj.username != undefined && obj.password != undefined
				&& obj.username != "" && obj.password != "") {
		
			alert("sending " + data);
			
			$.ajax({
				type: 'POST',
				data: data,
				contentType: 'application/json',
				url: '/dataMerger/users/authentication',
				dataType: 'json',
				success: function (data, textStatus, jqXHR) {
					
					if (data.redirectURL) {
						
						window.location.href = data.redirectURL;
		
					} else {
						alert("data: " + data);
						alert("data.redirectURL: " + data.redirectURL);
						alert("data.username: " + data.username);
						$('.status').html("textStatus: " + textStatus);
					}
				},
				error: function (jqXHR, textStatus, errorThrown){
		            $('.error').html("errorThrown: " + errorThrown);
		            $('.status').html("textStatus: " + textStatus);
		        },
				beforeSend: function() { $('.authenticating-indicator').show(); },
		        complete: function() { $('.authenticating-indicator').hide(); }
			});
		
		} else {
			alert("Please provide a username and password.");
		}
		
	});
}