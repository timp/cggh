function initAdminFunctions () {

	initCreateDatabaseFunction();
	initDeleteDatabaseFunction();
	
	initCreateDatabaseTablesFunction();
	initDeleteDatabaseTablesFunction();
	
	initCreateFilebaseFunction();
	initDeleteFilebaseFunction();
	
	initCreateFilebaseDirectoriesFunction();
	initDeleteFilebaseDirectoriesFunction();
}

function initCreateDatabaseFunction () {
	
	$('.createDatabaseButton').click(
			
			function () {
				
				$.ajax({
					  type: "POST",
					  data: '',
					  url: "/dataMerger/data/databases",
					  dataType: "text",
					  success: function(data, textStatus, jqXHR) {
							$('.ajaxResponse').html("Response: " + data);
							$('.refreshButtonContainer').show();
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

function initDeleteDatabaseFunction () {
	
	$('.deleteDatabaseButton').click(
			
			function () {
				
				$.ajax({
					  type: "DELETE",
					  data: '',
					  url: "/dataMerger/data/databases",
					  dataType: "text",
					  success: function(data, textStatus, jqXHR) {
							$('.ajaxResponse').html("Response: " + data);
							$('.refreshButtonContainer').show();
					  },
					  error:function (jqXHR, textStatus, errorThrown){
							$('.ajaxError').html("Error: " + errorThrown);
			          },
						beforeSend: function() { $('.deleting-database-indicator').show(); },
				        complete: function() { $('.deleting-database-indicator').hide(); }
					});
			}
			
		);
	
}


function initCreateDatabaseTablesFunction () {
	
	$('.createAndInitializeDatabaseTablesButton').click(
			
			function () {
				
				$.ajax({
					  type: "POST",
					  data: '',
					  url: "/dataMerger/scripts/data/databases/tables/create-and-initialize",
					  dataType: "text",
					  success: function(data, textStatus, jqXHR) {
							$('.ajaxResponse').html("Response: " + data);
							$('.refreshButtonContainer').show();
					  },
					  error:function (jqXHR, textStatus, errorThrown){
							$('.ajaxError').html("Error: " + errorThrown);
			          },
						beforeSend: function() { $('.creating-database-tables-indicator').show(); },
				        complete: function() { $('.creating-database-tables-indicator').hide(); }
					});
			}
			
		);
}

function initDeleteDatabaseTablesFunction () {
	
	$('.deleteDatabaseTablesButton').click(
			
			function () {
				
				$.ajax({
					  type: "DELETE",
					  data: '',
					  url: "/dataMerger/scripts/data/databases/tables/delete-all",
					  dataType: "text",
					  success: function(data, textStatus, jqXHR) {
							$('.ajaxResponse').html("Response: " + data);
							$('.refreshButtonContainer').show();
					  },
					  error:function (jqXHR, textStatus, errorThrown){
							$('.ajaxError').html("Error: " + errorThrown);
			          },
						beforeSend: function() { $('.deleting-database-tables-indicator').show(); },
				        complete: function() { $('.deleting-database-tables-indicator').hide(); }
					});
			}
			
		);
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
			
		);
	
}
