function initFilebaseFunctions () {

	initCreateFilebaseFunction();
	initDeleteFilebaseFunction();
	
	initCreateFilebaseDirectoriesFunction();
	initDeleteFilebaseDirectoriesFunction();
}




function initCreateFilebaseFunction () {
	
	$('.createFilebaseButton').click(
			
			function () {
				
				$.ajax({
					  type: "POST",
					  data: '',
					  url: "/dataMerger/files/filebases",
					  dataType: "text",
					  success: function(data, textStatus, jqXHR) {
							$('.ajaxResponse').html("Response: " + data);
							$('.refreshButtonContainer').show();
					  },
					  error:function (jqXHR, textStatus, errorThrown){
							$('.ajaxError').html("Error: " + errorThrown);
			          },
						beforeSend: function() { $('.creating-filebase-indicator').show(); },
				        complete: function() { $('.creating-filebase-indicator').hide(); }
					});
			}
			
		);
	
}


function initDeleteFilebaseFunction () {
	
	$('.deleteFilebaseButton').click(
			
			function () {

				
				if (confirm("Are you sure you want to delete all the files?")) {
					
					if (confirm("Are you SURE you're sure? You want to delete all the files?")) {
					
				
						$.ajax({
							  type: "DELETE",
							  data: '',
							  url: "/dataMerger/files/filebases",
							  dataType: "text",
							  success: function(data, textStatus, jqXHR) {
									$('.ajaxResponse').html("Response: " + data);
									$('.refreshButtonContainer').show();
							  },
							  error:function (jqXHR, textStatus, errorThrown){
									$('.ajaxError').html("Error: " + errorThrown);
					          },
								beforeSend: function() { $('.deleting-filebase-indicator').show(); },
						        complete: function() { $('.deleting-filebase-indicator').hide(); }
							});
					}
					
				}
				
				
			}
			
		);
	
}

function initCreateFilebaseDirectoriesFunction () {

	$('.createFilebaseDirectoriesButton').click(
			
			function () {
				
				$.ajax({
					  type: "POST",
					  data: '',
					  url: "/dataMerger/scripts/files/filebases/directories/create-and-initialize",
					  dataType: "text",
					  success: function(data, textStatus, jqXHR) {
							$('.ajaxResponse').html("Response: " + data);
							$('.refreshButtonContainer').show();
					  },
					  error:function (jqXHR, textStatus, errorThrown){
							$('.ajaxError').html("Error: " + errorThrown);
			          },
						beforeSend: function() { $('.creating-filebase-directories-indicator').show(); },
				        complete: function() { $('.creating-filebase-directories-indicator').hide(); }
					});
				
			}
			
		);
	
}

function initDeleteFilebaseDirectoriesFunction () {
	
	$('.deleteFilebaseDirectoriesButton').click(
			
			function () {
				
				if (confirm("Are you sure you want to delete all the files?")) {
					
					if (confirm("Are you SURE you're sure? You want to delete all the files?")) {
				
						$.ajax({
						  type: "DELETE",
						  data: '',
						  url: "/dataMerger/scripts/files/filebases/directories/delete-all",
						  dataType: "text",
						  success: function(data, textStatus, jqXHR) {
								$('.ajaxResponse').html("Response: " + data);
								$('.refreshButtonContainer').show();
						  },
						  error:function (jqXHR, textStatus, errorThrown){
								$('.ajaxError').html("Error: " + errorThrown);
				          },
							beforeSend: function() { $('.deleting-filebase-directories-indicator').show(); },
					        complete: function() { $('.deleting-filebase-directories-indicator').hide(); }
						});
					
					}
					
				}
			}
			
		);
	
}
