function initDatabaseFunctions () {

	initCreateDatabaseFunction();
	initDeleteDatabaseFunction();
	
	initCreateDatabaseTablesFunction();
	initDeleteDatabaseTablesFunction();
	
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
				
				if (confirm("Are you sure you want to delete the entire database?")) {
				
					if (confirm("Are you SURE you're sure? You want to delete the entire database?")) {
					
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
						
					
				}
				
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
				
				if (confirm("Are you sure you want to delete all the database tables?")) {
					
					if (confirm("Are you SURE you're sure? You want to delete all the database tables?")) {
					
						
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
			
				}
				
			}
					
		);
}



