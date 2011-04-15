function initAdminFunctions () {

	initCreateDatabaseFunction();
}

function initCreateDatabaseFunction () {
	
	$('.create-database-button').click(
			
			function () {
				
				$.ajax({
					  type: "POST",
					  data: '',
					  url: "/dataMerger/data/databases",
					  dataType: "text",
					  success: function(data, textStatus, jqXHR) {
							$('.ajaxResponse').html("Response: " + data);
					  },
					  error:function (jqXHR, textStatus, errorThrown){
							$('.ajaxError').html("Error: " + errorThrown);
			          },
						beforeSend: function() { $('.creating-database-indicator').show(); },
				        complete: function() { $('.creating-database-indicator').hide(); }
					});
			}
			
		);
	
}

$('.install-1_0-button').click(
		function () {

			
			$.ajax({
				  type: "POST",
				  url: "/dataMerger/scripts/install-v1.0",
				  dataType: "html",
				  success: function(data, textStatus, jqXHR) {
						$('.response').html("Response: " + data);
						$('.status').html("textStatus: " + textStatus);
				  },
				  error:function (jqXHR, textStatus, errorThrown){
						$('.response').html("Response: " + data);
		                $('.error').html("errorThrown: " + errorThrown);
		                $('.status').html("textStatus: " + textStatus);
		          } 
				});
		}
		
	);
	$('.uninstall-1_0-button').click(
			function () {
	
				
				$.ajax({
					  type: "POST",
					  url: "/dataMerger/scripts/uninstall-v1.0",
					  dataType: "html",
					  success: function(data, textStatus, jqXHR) {
							$('.response').html("Response: " + data);
							$('.status').html("textStatus: " + textStatus);
					  },
					  error:function (jqXHR, textStatus, errorThrown){
							$('.response').html("Response: " + data);
			                $('.error').html("errorThrown: " + errorThrown);
			                $('.status').html("textStatus: " + textStatus);
			          } 
					});
			}
			
		);